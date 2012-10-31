package org.hbird.parameterstorage.simple.query;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.QueryCallback;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleQuerySupportTest {

    private static final Long NOW = System.currentTimeMillis();

    @Mock
    private ParameterValueSeries<Double> series;

    @Mock
    private ParameterValueInTime<Double> value1;

    @Mock
    private ParameterValueInTime<Double> value2;

    @Mock
    private ParameterValueInTime<Double> value3;

    @Mock
    private QueryCallback<Double> callback;

    @Mock
    private Filter<Double> filter;

    @Mock
    private Filter<Double> continueFilter;

    private SimpleQuerySupport<Double> query;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        query = new SimpleQuerySupport<Double>(series);
        inOrder = inOrder(series, value1, value2, value3, callback, filter, continueFilter);
        when(series.getLastValue()).thenReturn(value3);
        when(value3.getNext()).thenReturn(value2);
        when(value2.getNext()).thenReturn(value1);
        when(value1.getNext()).thenReturn(null);
        when(filter.accept(value3)).thenReturn(true);
        when(filter.accept(value2)).thenReturn(true);
        when(filter.accept(value1)).thenReturn(true);
        when(series.next(value3)).thenReturn(value2);
        when(series.next(value2)).thenReturn(value1);
        when(series.next(value1)).thenReturn(null);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#SimpleQuerySupport(org.hbird.parameterstorage.api.ParameterValueSeries)}
     * .
     */
    @Test
    public void testSimpleQuerySupport() {
        testGetAll();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetAll() {
        query.get(filter, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetNone() {
        when(filter.accept(any(ParameterValueInTime.class))).thenReturn(false);
        query.get(filter, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetSome() {
        when(filter.accept(value2)).thenReturn(false);
        query.get(filter, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetLatest() {
        query.getLatest(filter, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetLatestRejectingFirst() {
        when(filter.accept(value3)).thenReturn(false);
        query.getLatest(filter, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetLatestRejectingAll() {
        when(filter.accept(any(ParameterValueInTime.class))).thenReturn(false);
        query.getLatest(filter, callback);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterAcceptAll() {
        when(value3.getTimeStamp()).thenReturn(NOW + 3);
        when(value2.getTimeStamp()).thenReturn(NOW + 2);
        when(value1.getTimeStamp()).thenReturn(NOW + 1);
        query.getAfter(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(value3, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(value2, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(value1, times(1)).getTimeStamp();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterAcceptSome() {
        when(value3.getTimeStamp()).thenReturn(NOW + 1);
        when(value2.getTimeStamp()).thenReturn(NOW);
        when(value1.getTimeStamp()).thenReturn(NOW - 1);
        query.getAfter(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(value3, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(value2, times(1)).getTimeStamp();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfterAcceptNone() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getAfter(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(value3, times(1)).getTimeStamp();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterAllAfter() {
        when(value3.getTimeStamp()).thenReturn(NOW + 3);
        when(value2.getTimeStamp()).thenReturn(NOW + 2);
        when(value1.getTimeStamp()).thenReturn(NOW + 1);
        query.getAfter(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(value3, times(1)).getTimeStamp();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterOneAfter() {
        when(value3.getTimeStamp()).thenReturn(NOW + 1);
        when(value2.getTimeStamp()).thenReturn(NOW);
        when(value1.getTimeStamp()).thenReturn(NOW - 1);
        query.getAfter(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(value3, times(1)).getTimeStamp();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfterNoneAfter() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getAfter(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeAll() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBefore(filter, callback, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeLast() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBefore(filter, callback, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeSome() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBefore(filter, callback, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeFirst() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBefore(filter, callback, NOW - 2);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBeforeAny() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBefore(filter, callback, NOW - 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(series, times(1)).next(value1);
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
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBefore(filter, callback, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
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
    public void testGetLatestBeforeSome() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBefore(filter, callback, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
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
    public void testGetLatestBeforeFirst() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBefore(filter, callback, NOW - 2);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
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
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBefore(filter, callback, NOW - 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
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
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 3, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenEnds() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 2, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(value2, times(1)).getTimeStamp();
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
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 1, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
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
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW + 1, NOW + 2);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
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
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 5, NOW - 3);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenWrongOrder() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 3, NOW - 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenEndOutOfRange() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 1, NOW + 5);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetweenStartOutOfRange() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getBetween(filter, callback, NOW - 5, NOW - 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(series, times(1)).next(value2);
        inOrder.verify(filter, times(1)).accept(value1);
        inOrder.verify(callback, times(1)).onValue(value1);
        inOrder.verify(series, times(1)).next(value1);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenAll() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBetween(filter, callback, NOW - 3, NOW + 1);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenEnds() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBetween(filter, callback, NOW - 2, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onValue(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#getLatestBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetweenNeighbours() {
        when(value3.getTimeStamp()).thenReturn(NOW);
        when(value2.getTimeStamp()).thenReturn(NOW - 1);
        when(value1.getTimeStamp()).thenReturn(NOW - 2);
        query.getLatestBetween(filter, callback, NOW - 1, NOW);
        inOrder.verify(callback, times(1)).onStart();
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verify(filter, times(1)).accept(value2);
        inOrder.verify(callback, times(1)).onEnd();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#select(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectValueNull() {
        when(series.getLastValue()).thenReturn(null);
        query.select(series, filter, callback, filter);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#select(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFilterRejectContinueFilterAccept() {
        when(series.next(value3)).thenReturn(null);
        when(filter.accept(value3)).thenReturn(false);
        when(continueFilter.accept(value3)).thenReturn(true);
        query.select(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(continueFilter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#select(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFilterRejectContinueFilterReject() {
        when(filter.accept(value3)).thenReturn(false);
        when(continueFilter.accept(value3)).thenReturn(false);
        query.select(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(continueFilter, times(1)).accept(value3);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#select(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFilterAcceptContinueFilterAccept() {
        when(series.next(value3)).thenReturn(null);
        when(filter.accept(value3)).thenReturn(true);
        when(continueFilter.accept(value3)).thenReturn(true);
        query.select(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(continueFilter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#select(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFilterAcceptContinueFilterReject() {
        when(series.next(value3)).thenReturn(null);
        when(filter.accept(value3)).thenReturn(true);
        when(continueFilter.accept(value3)).thenReturn(false);
        query.select(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verify(continueFilter, times(1)).accept(value3);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#selectFirst(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFirstValueNull() {
        when(series.getLastValue()).thenReturn(null);
        query.selectFirst(series, filter, callback, filter);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#selectFirst(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFirstFilterRejectContinueAccept() {
        when(series.next(value3)).thenReturn(null);
        when(filter.accept(value3)).thenReturn(false);
        when(continueFilter.accept(value3)).thenReturn(true);
        query.selectFirst(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(continueFilter, times(1)).accept(value3);
        inOrder.verify(series, times(1)).next(value3);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#selectFirst(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFirstFilterRejectContinueReject() {
        when(series.next(value3)).thenReturn(null);
        when(filter.accept(value3)).thenReturn(false);
        when(continueFilter.accept(value3)).thenReturn(false);
        query.selectFirst(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(continueFilter, times(1)).accept(value3);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SimpleQuerySupport#selectFirst(org.hbird.parameterstorage.api.ParameterValueInTime, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @Test
    public void testSelectFirstFilterAcceptContinueAccept() {
        when(series.next(value3)).thenReturn(null);
        when(filter.accept(value1)).thenReturn(true);
        when(continueFilter.accept(value3)).thenReturn(true);
        query.selectFirst(series, filter, callback, continueFilter);
        inOrder.verify(filter, times(1)).accept(value3);
        inOrder.verify(callback, times(1)).onValue(value3);
        inOrder.verifyNoMoreInteractions();
    }
}
