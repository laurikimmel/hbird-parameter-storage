package org.hbird.parameterstorage.simple;

import java.util.List;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemperatureCrudSupportTest {

    private static final Long NOW = System.currentTimeMillis();

    private ParameterValueSeries<Double> temperatures;
    private SimpleCrudSupport<Double> crud;
    private ParameterValueInTimeFactory<Double> factory;

    @Mock
    private CallbackWithValue<ParameterValueInTime<Double>> callback;

    @Mock
    private CallbackWithValue<ParameterValueInTime<Double>> callback2;

    @Mock
    private CallbackWithValue<Long> counter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        temperatures = new SimpleParameterValueSeries<Double>();
        factory = new SimpleParameterValueInTimeFactory<Double>();
        crud = new SimpleCrudSupport<Double>(temperatures, factory);
        inOrder = inOrder(callback, counter, callback2);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#add(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testAddWidthException() {
        crud.add(null, null, callback);
        temperatures.count(counter);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verify(counter, times(1)).onValue(0L);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#add(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testAdd() {
        crud.add(NOW, 300D, callback);
        crud.add(NOW + 1, 301D, callback);
        crud.add(NOW + 2, 302D, callback);
        temperatures.count(counter);
        ArgumentCaptor<ParameterValueInTime> captor = ArgumentCaptor.forClass(ParameterValueInTime.class);
        inOrder.verify(callback, times(3)).onValue(captor.capture());
        inOrder.verify(counter, times(1)).onValue(3L);
        inOrder.verifyNoMoreInteractions();
        List<ParameterValueInTime> list = captor.getAllValues();
        ParameterValueInTime<Double> value = list.get(0);
        assertEquals(NOW, value.getTimeStamp());
        assertEquals(300D, value.getValue(), 0.0D);
        assertNull(value.getNext());
        value = list.get(1);
        assertEquals(new Long(NOW + 1), value.getTimeStamp());
        assertEquals(301D, value.getValue(), 0.0D);
        assertNotNull(value.getNext());
        value = list.get(2);
        assertEquals(new Long(NOW + 2), value.getTimeStamp());
        assertEquals(302D, value.getValue(), 0.0D);
        assertNotNull(value.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGetNoValues() {
        crud.get(NOW, callback);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetValueNotFound() {
        crud.add(NOW, 4.34D, callback);
        crud.get(NOW + 1, callback);
        inOrder.verify(callback, times(1)).onValue(any(ParameterValueInTime.class));
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testGetValue() {
        crud.add(NOW, 4.34D, callback2);
        crud.add(NOW + 1, 4.343D, callback2);
        crud.add(NOW + 2, 4.32D, callback2);
        crud.get(NOW + 2, callback);
        crud.get(NOW, callback);
        crud.get(NOW + 1, callback);
        inOrder.verify(callback2, times(3)).onValue(any(ParameterValueInTime.class));
        ArgumentCaptor<ParameterValueInTime> captor = ArgumentCaptor.forClass(ParameterValueInTime.class);
        inOrder.verify(callback, times(3)).onValue(captor.capture());
        inOrder.verifyNoMoreInteractions();
        ParameterValueInTime<Double> val = captor.getAllValues().get(0);
        assertEquals(new Long(NOW + 2), val.getTimeStamp());
        assertEquals(4.32D, val.getValue(), 0.0D);
        assertNotNull(val.getNext());
        val = captor.getAllValues().get(1);
        assertEquals(new Long(NOW), val.getTimeStamp());
        assertEquals(4.34D, val.getValue(), 0.0D);
        assertNull(val.getNext());
        val = captor.getAllValues().get(2);
        assertEquals(new Long(NOW + 1), val.getTimeStamp());
        assertEquals(4.343D, val.getValue(), 0.0D);
        assertNotNull(val.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testUpdateNoValues() {
        crud.update(NOW, 1000D, callback);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdateValueNotFound() {
        crud.add(NOW, 1.0D, callback2);
        crud.add(NOW + 1, 2.0D, callback2);
        crud.add(NOW + 2, 3.0D, callback2);
        crud.update(NOW - 1, 1000D, callback);
        inOrder.verify(callback2, times(3)).onValue(any(ParameterValueInTime.class));
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testUpdate() {
        crud.add(NOW, 100.1D, callback2);
        crud.add(NOW + 1, 200.2D, callback2);
        crud.add(NOW + 2, 300.3D, callback2);
        crud.update(NOW, 100.2D, callback);
        crud.get(NOW, callback);
        crud.update(NOW + 1, -200.2D, callback);
        crud.get(NOW + 1, callback);
        crud.update(NOW + 2, 0.0D, callback);
        crud.get(NOW + 2, callback);

        inOrder.verify(callback2, times(3)).onValue(any(ParameterValueInTime.class));
        ArgumentCaptor<ParameterValueInTime> captor = ArgumentCaptor.forClass(ParameterValueInTime.class);
        inOrder.verify(callback, times(6)).onValue(captor.capture());
        inOrder.verifyNoMoreInteractions();
        ParameterValueInTime<Double> val = captor.getAllValues().get(0);
        assertEquals(new Long(NOW), val.getTimeStamp());
        assertEquals(100.2D, val.getValue(), 0.0D);
        assertNull(val.getNext());
        val = captor.getAllValues().get(1);
        assertEquals(new Long(NOW), val.getTimeStamp());
        assertEquals(100.2D, val.getValue(), 0.0D);
        assertNull(val.getNext());
        val = captor.getAllValues().get(2);
        assertEquals(new Long(NOW + 1), val.getTimeStamp());
        assertEquals(-200.2D, val.getValue(), 0.0D);
        assertNotNull(val.getNext());
        val = captor.getAllValues().get(3);
        assertEquals(new Long(NOW + 1), val.getTimeStamp());
        assertEquals(-200.2D, val.getValue(), 0.0D);
        assertNotNull(val.getNext());
        val = captor.getAllValues().get(4);
        assertEquals(new Long(NOW + 2), val.getTimeStamp());
        assertEquals(0.0D, val.getValue(), 0.0D);
        assertNotNull(val.getNext());
        val = captor.getAllValues().get(5);
        assertEquals(new Long(NOW + 2), val.getTimeStamp());
        assertEquals(0.0D, val.getValue(), 0.0D);
        assertNotNull(val.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testDeleteNoValues() {
        crud.delete(NOW, callback);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteValueNotFound() {
        crud.add(NOW, -123D, callback2);
        crud.add(NOW + 1, -123.4D, callback2);
        crud.add(NOW + 2, -123.456D, callback2);
        crud.delete(NOW - 1, callback);
        inOrder.verify(callback2, times(3)).onValue(any(ParameterValueInTime.class));
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testDelete() {
        crud.add(NOW, -123D, callback2);
        crud.add(NOW + 1, -123.4D, callback2);
        crud.add(NOW + 2, -123.456D, callback2);
        crud.delete(NOW + 1, callback);
        crud.get(NOW + 1, callback);
        crud.delete(NOW + 2, callback);
        crud.get(NOW + 2, callback);
        crud.delete(NOW, callback);
        crud.get(NOW, callback);
        inOrder.verify(callback2, times(3)).onValue(any(ParameterValueInTime.class));

        ArgumentCaptor<ParameterValueInTime> captor = ArgumentCaptor.forClass(ParameterValueInTime.class);
        inOrder.verify(callback, times(1)).onValue(captor.capture());
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verify(callback, times(1)).onValue(captor.capture());
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verify(callback, times(1)).onValue(captor.capture());
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();

        ParameterValueInTime<Double> val = captor.getAllValues().get(0);
        assertEquals(new Long(NOW + 1), val.getTimeStamp());
        assertEquals(-123.4D, val.getValue(), 0.0D);
        assertNull(val.getNext());

        val = captor.getAllValues().get(1);
        assertEquals(new Long(NOW + 2), val.getTimeStamp());
        assertEquals(-123.456D, val.getValue(), 0.0D);
        assertNull(val.getNext());

        val = captor.getAllValues().get(2);
        assertEquals(new Long(NOW), val.getTimeStamp());
        assertEquals(-123D, val.getValue(), 0.0D);
        assertNull(val.getNext());
    }
}
