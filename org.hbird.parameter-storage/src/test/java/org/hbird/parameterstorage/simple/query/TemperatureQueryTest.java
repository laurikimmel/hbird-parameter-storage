package org.hbird.parameterstorage.simple.query;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.QueryCallback;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.hbird.parameterstorage.simple.query.filter.Filters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemperatureQueryTest {

    public static final Long NOW = System.currentTimeMillis();

    private ParameterValueSeries<Double> series;
    private SimpleQuerySupport<Double> query;
    private Filter<Double> aboveZero;
    private Filter<Double> belowZero;

    private ParameterValueInTime<Double> value1;
    private ParameterValueInTime<Double> value2;
    private ParameterValueInTime<Double> value3;
    private ParameterValueInTime<Double> value4;
    private ParameterValueInTime<Double> value5;

    @Mock
    private QueryCallback<Double> callback;

    private InOrder inOrder;

    private Filter<Double> acceptAll;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        series = new SimpleParameterValueSeries<Double>();
        query = new SimpleQuerySupport<Double>(series);
        inOrder = inOrder(callback);

        value1 = new SimpleParameterValueInTime<Double>(-1.5D, NOW);
        value2 = new SimpleParameterValueInTime<Double>(-0.5D, NOW + 1);
        value3 = new SimpleParameterValueInTime<Double>(-0.0D, NOW + 2);
        value4 = new SimpleParameterValueInTime<Double>(0.345D, NOW + 3);
        value5 = new SimpleParameterValueInTime<Double>(1.993D, NOW + 4);

        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        series.addValue(value4);
        series.addValue(value5);

        aboveZero = new Filter<Double>() {
            @Override
            public boolean accept(ParameterValueInTime<Double> value) {
                return value.getValue() > 0.0D;
            }
        };

        belowZero = new Filter<Double>() {
            @Override
            public boolean accept(ParameterValueInTime<Double> value) {
                return value.getValue() < 0.0D;
            }
        };

        acceptAll = Filters.acceptAll();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetAll() {
        query.get(acceptAll, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetAllAboveZero() {
        query.get(aboveZero, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetAllBelowZero() {
        query.get(belowZero, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetLatestFromAll() {
        query.getLatest(acceptAll, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetLatestFromAboveZero() {
        query.getLatest(aboveZero, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetLatestFromBelowZero() {
        query.getLatest(belowZero, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterReturnAll() {
        query.getAfter(acceptAll, callback, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterReturnAllExpectLast() {
        query.getAfter(acceptAll, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterReturnSome() {
        query.getAfter(acceptAll, callback, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterReturnFirst() {
        query.getAfter(acceptAll, callback, NOW + 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterReturnNone() {
        query.getAfter(acceptAll, callback, NOW + 4);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterReturnNoneAtAll() {
        query.getAfter(acceptAll, callback, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterAll() {
        query.getLatestAfter(acceptAll, callback, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterLast() {
        query.getLatestAfter(acceptAll, callback, NOW + 4);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterSome() {
        query.getLatestAfter(acceptAll, callback, NOW + 2);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterAny() {
        query.getLatestAfter(acceptAll, callback, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeReturnAll() {
        query.getBefore(acceptAll, callback, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeReturnAllExpectFirst() {
        query.getBefore(acceptAll, callback, NOW + 4);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeReturnSome() {
        query.getBefore(acceptAll, callback, NOW + 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeReturnLast() {
        query.getBefore(acceptAll, callback, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeReturnNone() {
        query.getBefore(acceptAll, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeReturnNoneAtAll() {
        query.getBefore(acceptAll, callback, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBeforeAtAll() {
        query.getLatestBefore(acceptAll, callback, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBeforeAll() {
        query.getLatestBefore(acceptAll, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBeforeOne() {
        query.getLatestBefore(acceptAll, callback, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBeforeThree() {
        query.getLatestBefore(acceptAll, callback, NOW + 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBeforeLates() {
        query.getLatestBefore(acceptAll, callback, NOW + 4);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBeforeAny() {
        query.getLatestBefore(acceptAll, callback, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenAll() {
        query.getBetween(acceptAll, callback, NOW - 1, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenBothEnds() {
        query.getBetween(acceptAll, callback, NOW, NOW + 4);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenSome() {
        query.getBetween(acceptAll, callback, NOW + 1, NOW + 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenNeighbours() {
        query.getBetween(acceptAll, callback, NOW + 1, NOW + 2);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenBothAfter() {
        query.getBetween(acceptAll, callback, NOW + 10, NOW + 20);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenBothBefore() {
        query.getBetween(acceptAll, callback, NOW - 20, NOW - 10);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenBothAfterWrongOrder() {
        query.getBetween(acceptAll, callback, NOW + 20, NOW + 10);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenBothBeforeWrongOrder() {
        query.getBetween(acceptAll, callback, NOW - 10, NOW - 20);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenAllWrongOrder() {
        query.getBetween(acceptAll, callback, NOW + 5, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getFirstBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenAll() {
        query.getBetween(acceptAll, callback, NOW - 1, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value5);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getFirstBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenBothEnds() {
        query.getBetween(acceptAll, callback, NOW, NOW + 4);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value4);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getFirstBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenSome() {
        query.getBetween(acceptAll, callback, NOW + 1, NOW + 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getFirstBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenNeighbours() {
        query.getBetween(acceptAll, callback, NOW + 2, NOW + 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }
}
