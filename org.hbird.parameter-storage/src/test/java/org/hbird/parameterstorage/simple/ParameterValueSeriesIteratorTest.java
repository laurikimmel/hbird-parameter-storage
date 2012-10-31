package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.simple.ParameterValueSeriesIterator;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParameterValueSeriesIteratorTest {

    private static final Long NOW = System.currentTimeMillis();

    private ParameterValueSeries<Object> series;
    private ParameterValueSeriesIterator<Object> iterator;
    private ParameterValueInTime<Object> value1;
    private ParameterValueInTime<Object> value2;
    private ParameterValueInTime<Object> value3;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        series = new SimpleParameterValueSeries<Object>();
        iterator = new ParameterValueSeriesIterator<Object>(series);
        value1 = new SimpleParameterValueInTime<Object>("A", NOW);
        value2 = new SimpleParameterValueInTime<Object>("B", NOW + 1);
        value3 = new SimpleParameterValueInTime<Object>("C", NOW + 2);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.ParameterValueSeriesIterator#ParameterValueSeriesIterator(org.hbird.parameterstorage.api.ParameterValueSeries)}
     * .
     */
    @Test
    public void testParameterValueSeriesIterator() {
        new ParameterValueSeriesIterator<Object>(series);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.ParameterValueSeriesIterator#ParameterValueSeriesIterator(org.hbird.parameterstorage.api.ParameterValueSeries)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testParameterValueSeriesIteratorWithNullSeries() {
        new ParameterValueSeriesIterator<Object>(null);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.ParameterValueSeriesIterator#hasNext()}.
     */
    @Test
    public void testHasNext() {
        assertFalse(iterator.hasNext());
        series.addValue(value1);
        assertTrue(iterator.hasNext());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.ParameterValueSeriesIterator#next()}.
     */
    @Test
    public void testNext() {
        assertNull(iterator.next());
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        assertTrue(iterator.hasNext());
        assertEquals(value3, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(value2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(value1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.ParameterValueSeriesIterator#remove()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }
}
