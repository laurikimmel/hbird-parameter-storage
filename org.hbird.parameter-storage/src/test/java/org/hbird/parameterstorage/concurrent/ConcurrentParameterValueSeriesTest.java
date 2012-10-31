package org.hbird.parameterstorage.concurrent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime;
import org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries;
import org.hbird.parameterstorage.simple.query.SimpleQuerySupport;
import org.hbird.parameterstorage.simple.query.SyncQuerySupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


import static org.hbird.parameterstorage.simple.query.filter.Filters.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ConcurrentParameterValueSeriesTest {

    private static final Long NOW = System.currentTimeMillis();

    private AtomicParameterValueInTime<Object> value1;
    private AtomicParameterValueInTime<Object> value2;
    private AtomicParameterValueInTime<Object> value3;

    private ConcurrentParameterValueSeries<Object> series;

    private SyncQuerySupport<Object> query;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        series = new ConcurrentParameterValueSeries<Object>();
        value1 = new AtomicParameterValueInTime<Object>("A", NOW);
        value2 = new AtomicParameterValueInTime<Object>("B", NOW + 1);
        value3 = new AtomicParameterValueInTime<Object>("C", NOW + 2);
        query = new SyncQuerySupport<Object>(new SimpleQuerySupport<Object>(series));
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#AtomicParamterValueSeries(org.hbird.parameterstorage.api.ParameterDefinition)}
     * .
     */
    @Test
    public void testAtomicParamterValueSeries() {
        assertNull(series.getLastValue());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#getLastValue()}.
     */
    @Test
    public void testGetLastValue() {
        testAddValueInOrder();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#addValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAddValueInOrder() {
        assertNull(series.getLastValue());
        assertNull(series.addValue(value1));
        assertEquals(value1, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertNull(series.addValue(value2));
        assertEquals(value2, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertNull(series.addValue(value3));
        verifyAllAdded();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#addValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAddValueInReverseOrder() {
        assertNull(series.getLastValue());
        assertNull(series.addValue(value3));
        assertEquals(value3, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertEquals(value3, series.addValue(value2));
        assertEquals(value3, series.getLastValue());
        assertEquals(value2, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertEquals(value2, series.addValue(value1));
        verifyAllAdded();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#addValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAddValueInRandomOrder1() {
        assertNull(series.getLastValue());
        assertNull(series.addValue(value2));
        assertEquals(value2, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertNull(series.addValue(value3));
        assertEquals(value3, series.getLastValue());
        assertEquals(value2, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertEquals(value2, series.addValue(value1));
        verifyAllAdded();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#addValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAddValueInRandomOrder2() {
        assertNull(series.getLastValue());
        assertNull(series.addValue(value1));
        assertEquals(value1, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertNull(series.addValue(value3));
        assertEquals(value3, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertEquals(value3, series.addValue(value2));
        verifyAllAdded();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#addValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testAddValueInRandomOrder3() {
        List<AtomicParameterValueInTime<Object>> list = Arrays.asList(value1, value2, value3);
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            series.addValue(list.get(i));
            assertEquals(i + 1, query.count(acceptAll()));
        }
        verifyAllAdded();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#addValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueTwice() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        series.addValue(value1);
    }

    /**
     * 
     */
    private void verifyAllAdded() {
        assertEquals(3, query.count(acceptAll()));
        assertEquals(value3, series.getLastValue());
        assertEquals(value2, series.getLastValue().getNext());
        assertEquals(value1, series.getLastValue().getNext().getNext());
        assertNull(series.getLastValue().getNext().getNext().getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#insert(org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime, org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime)}
     * .
     */
    @Test
    public void testInsert() {
        value3.setNext(value2);
        assertEquals(value2, value3.getNext());
        assertNull(value2.getNext());
        assertNull(value1.getNext());
        assertTrue(series.insert(value3, value2, value1));
        assertEquals(value1, value3.getNext());
        assertNull(value2.getNext());
        assertEquals(value2, value1.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#insert(org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime, org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime)}
     * .
     */
    @Test
    public void testInsertWrongOldValue() {
        value3.setNext(value2);
        assertEquals(value2, value3.getNext());
        assertNull(value2.getNext());
        assertNull(value1.getNext());
        assertFalse(series.insert(value3, value1, value1));
        assertEquals(value2, value3.getNext());
        assertNull(value2.getNext());
        assertNull(value1.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#findParentForAdd(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindParentForAdd() {
        assertEquals(series.head, series.findParentForAdd(value1));
        assertEquals(series.head, series.findParentForAdd(value2));
        assertEquals(series.head, series.findParentForAdd(value3));

        series.addValue(value1);
        assertNull(series.findParentForAdd(value1));
        assertEquals(series.head, series.findParentForAdd(value2));
        assertEquals(series.head, series.findParentForAdd(value3));

        series.addValue(value3);
        assertNull(series.findParentForAdd(value1));
        assertEquals(value3, series.findParentForAdd(value2));
        assertNull(series.findParentForAdd(value3));

        series.addValue(value2);
        assertNull(series.findParentForAdd(value1));
        assertNull(series.findParentForAdd(value2));
        assertNull(series.findParentForAdd(value3));

        series.removeValue(value3);
        assertNull(series.findParentForAdd(value1));
        assertNull(series.findParentForAdd(value2));
        assertEquals(series.head, series.findParentForAdd(value3));

        series.removeValue(value1);
        assertEquals(value2, series.findParentForAdd(value1));
        assertNull(series.findParentForAdd(value2));
        assertEquals(series.head, series.findParentForAdd(value3));
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#removeValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testRemoveValueFromHead() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        verifyAllAdded();
        assertNull(series.removeValue(value3));
        assertEquals(2, query.count(acceptAll()));
        assertEquals(value2, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertNull(series.removeValue(value2));
        assertEquals(1, query.count(acceptAll()));
        assertEquals(value1, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext());
        assertNull(series.removeValue(value1));
        assertEquals(0, query.count(acceptAll()));
        assertNull(series.getLastValue());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#removeValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testRemoveValueFromTail() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        verifyAllAdded();
        assertEquals(value2, series.removeValue(value1));
        assertEquals(2, query.count(acceptAll()));
        assertEquals(value3, series.getLastValue());
        assertEquals(value2, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertEquals(value3, series.removeValue(value2));
        assertEquals(1, query.count(acceptAll()));
        assertEquals(value3, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertNull(series.removeValue(value3));
        assertEquals(0, query.count(acceptAll()));
        assertNull(series.getLastValue());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#removeValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testRemoveValueFromMiddle() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        verifyAllAdded();
        assertEquals(value3, series.removeValue(value2));
        assertEquals(2, query.count(acceptAll()));
        assertEquals(value3, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());
        assertNull(series.removeValue(value3));
        assertEquals(1, query.count(acceptAll()));
        assertEquals(value1, series.getLastValue());
        assertNull(series.getLastValue().getNext());
        assertNull(series.removeValue(value1));
        assertEquals(0, query.count(acceptAll()));
        assertNull(series.getLastValue());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#removeValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveValueRandomOrder() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        verifyAllAdded();

        List<AtomicParameterValueInTime<Object>> list = Arrays.asList(value1, value2, value3);
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            series.removeValue(list.get(i));
            assertEquals(list.size() - (i + 1), query.count(acceptAll()));
        }
        assertEquals(0, query.count(acceptAll()));
        assertNull(series.getLastValue());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#removeValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveValueTwice() {
        series.addValue(value1);
        series.addValue(value2);
        series.removeValue(value1);
        series.removeValue(value1);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#removeValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNotExisitingValue() {
        series.addValue(value1);
        series.addValue(value2);
        series.removeValue(value3);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#findParentForRemove(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindParentForRemove() {
        assertNull(series.findParentForRemove(value1));
        assertNull(series.findParentForRemove(value2));
        assertNull(series.findParentForRemove(value3));

        series.addValue(value1);
        assertEquals(series.head, series.findParentForRemove(value1));
        assertNull(series.findParentForRemove(value2));
        assertNull(series.findParentForRemove(value3));

        series.addValue(value3);
        assertEquals(value3, series.findParentForRemove(value1));
        assertNull(series.findParentForRemove(value2));
        assertEquals(series.head, series.findParentForRemove(value3));

        series.addValue(value2);
        assertEquals(value2, series.findParentForRemove(value1));
        assertEquals(value3, series.findParentForRemove(value2));
        assertEquals(series.head, series.findParentForRemove(value3));

        series.removeValue(value3);
        assertEquals(value2, series.findParentForRemove(value1));
        assertEquals(series.head, series.findParentForRemove(value2));
        assertNull(series.findParentForRemove(value3));

        series.removeValue(value1);
        assertNull(series.findParentForRemove(value1));
        assertEquals(series.head, series.findParentForRemove(value2));
        assertNull(series.findParentForRemove(value3));

        series.removeValue(value2);
        assertNull(series.findParentForRemove(value1));
        assertNull(series.findParentForRemove(value2));
        assertNull(series.findParentForRemove(value3));
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries#count(org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCount() {
        assertEquals(0, query.count(acceptAll()));
        series.addValue(value1);
        assertEquals(1, query.count(acceptAll()));
        series.addValue(value2);
        assertEquals(2, query.count(acceptAll()));
        series.addValue(value3);
        assertEquals(3, query.count(acceptAll()));
    }
}
