/** 
 *
 */
package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleParameterValueSeriesTest {

    private SimpleParameterValueSeries<Double> series;
    private ParameterValueInTime<Double> value1;
    private ParameterValueInTime<Double> value2;
    private ParameterValueInTime<Double> value3;

    @Mock
    private CallbackWithValue<Long> callback;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        series = new SimpleParameterValueSeries<Double>();
        long now = System.currentTimeMillis();
        value1 = new SimpleParameterValueInTime<Double>(1.0D, now);
        value2 = new SimpleParameterValueInTime<Double>(1.1D, now + 1);
        value3 = new SimpleParameterValueInTime<Double>(0.9D, now - 1);
        inOrder = inOrder(callback);
    }

    @Test
    public void testAddValue() {
        assertNull(series.getLastValue());

        assertEquals(value1, series.addValue(value1));
        assertEquals(value1, series.getLastValue());
        try {
            assertNull(series.addValue(value1));
            fail("Exception expexted");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }

        assertEquals(value2, series.addValue(value2));
        assertEquals(value2, series.getLastValue());
        try {
            assertNull(series.addValue(value2));
            fail("Exception expexted");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }

        assertEquals(value2, series.addValue(value3));
        assertEquals(value2, series.getLastValue());
        try {
            assertNull(series.addValue(value3));
            fail("Exception expexted");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testRemoveValue() {

        assertNull(series.removeValue(value1));
        assertNull(series.removeValue(value2));
        assertNull(series.removeValue(value3));

        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);
        assertEquals(value1, series.removeValue(value2));
        assertEquals(value1, series.getLastValue());

        series.addValue(value2);
        assertEquals(value2, series.removeValue(value1));
        assertEquals(value2, series.getLastValue());

        series.addValue(value1);
        assertEquals(value1, series.removeValue(value3));
        assertEquals(value2, series.getLastValue());
    }

    @Test
    public void testRemoveFromHead() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);

        assertEquals(value2, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertEquals(value3, series.getLastValue().getNext().getNext());
        assertNull(series.getLastValue().getNext().getNext().getNext());

        assertEquals(value1, series.removeValue(value2));
        assertEquals(value3, series.removeValue(value1));
        assertNull(series.removeValue(value3));

    }

    @Test
    public void testRemoveFromTail() {
        series.addValue(value1);
        series.addValue(value2);
        series.addValue(value3);

        assertEquals(value2, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertEquals(value3, series.getLastValue().getNext().getNext());
        assertNull(series.getLastValue().getNext().getNext().getNext());

        assertEquals(value1, series.removeValue(value3));
        assertEquals(value2, series.removeValue(value1));
        assertNull(series.removeValue(value2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveFromUnknown() {
        series.addValue(value1);
        series.addValue(value2);

        assertEquals(value2, series.getLastValue());
        assertEquals(value1, series.getLastValue().getNext());
        assertNull(series.getLastValue().getNext().getNext());

        series.removeValue(value3);
    }

    @Test
    public void testCount() {
        series.count(callback);
        series.addValue(value1);
        series.count(callback);
        series.addValue(value2);
        series.count(callback);
        series.addValue(value3);
        series.count(callback);
        inOrder.verify(callback, times(1)).onValue(0L);
        inOrder.verify(callback, times(1)).onValue(1L);
        inOrder.verify(callback, times(1)).onValue(2L);
        inOrder.verify(callback, times(1)).onValue(3L);
        inOrder.verifyNoMoreInteractions();
    }
}
