package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.simple.SimpleCrudSupport;
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
public class SimpleCrudSupportTest {

    private static final Long NOW = System.currentTimeMillis();

    private static final Long VALUE = 991289231L;

    @Mock
    private ParameterValueInTimeFactory<Long> factory;

    @Mock
    private ParameterValueSeries<Long> series;

    @Mock
    private CallbackWithValue<ParameterValueInTime<Long>> callback;

    @Mock
    private IllegalArgumentException exception;

    @Mock
    private ParameterValueInTime<Long> paramValue;

    @Mock
    private ParameterValueInTime<Long> paramValue2;

    private SimpleCrudSupport<Long> crud;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        crud = new SimpleCrudSupport<Long>(series, factory);
        when(factory.create(NOW, VALUE)).thenReturn(paramValue);
        inOrder = inOrder(factory, series, callback, exception, paramValue, paramValue2);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#SimpleCrudSupport(org.hbird.parameterstorage.api.ParameterValueSeries, org.hbird.parameterstorage.api.ParameterValueInTimeFactory)}
     * .
     */
    @Test
    public void testSimpleCrudSupportParameterValueSeriesOfTParameterValueInTimeFactoryOfT() {
        testAdd();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#SimpleCrudSupport(org.hbird.parameterstorage.api.ParameterValueSeries, org.hbird.parameterstorage.api.ParameterValueInTimeFactory)}
     * .
     */
    @Test
    public void testSimpleCrudSupportParameterValueSeriesOfTParameterValueInTimeFactoryOfTWithInvalidValue() {
        testAddWithInvalidValue();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#add(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testAdd() {
        crud.add(NOW, VALUE, callback);
        inOrder.verify(factory, times(1)).create(NOW, VALUE);
        inOrder.verify(series, times(1)).addValue(paramValue);
        inOrder.verify(callback, times(1)).onValue(paramValue);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#add(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testAddWithInvalidValue() {
        when(factory.create(NOW, VALUE)).thenThrow(exception);
        crud.add(NOW, VALUE, callback);
        inOrder.verify(factory, times(1)).create(NOW, VALUE);
        inOrder.verify(callback, times(1)).onException(exception);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGetNoValues() {
        when(series.getLastValue()).thenReturn(null);
        crud.get(NOW, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGetValueNotFound() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW - 1);
        when(paramValue.getNext()).thenReturn(null);
        crud.get(NOW, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGet() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW);
        when(paramValue.getValue()).thenReturn(VALUE);
        when(paramValue.getNext()).thenReturn(paramValue);
        crud.get(NOW, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(callback, times(1)).onValue(paramValue);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testUpdateNoValues() {
        when(series.getLastValue()).thenReturn(null);
        crud.update(NOW, VALUE, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testUpdateValueNotFound() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW + 1);
        when(paramValue.getNext()).thenReturn(null);
        crud.update(NOW, VALUE, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testUpdateNewValueNotValid() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW);
        when(factory.create(NOW, VALUE)).thenThrow(exception);
        crud.update(NOW, VALUE, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(factory, times(1)).create(NOW, VALUE);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testUpdate() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW);
        when(factory.create(NOW, VALUE)).thenReturn(paramValue2);
        crud.update(NOW, VALUE, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(factory, times(1)).create(NOW, VALUE);
        inOrder.verify(series, times(1)).removeValue(paramValue);
        inOrder.verify(series, times(1)).addValue(paramValue2);
        inOrder.verify(callback, times(1)).onValue(paramValue2);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testDeleteNoValues() {
        when(series.getLastValue()).thenReturn(null);
        crud.delete(NOW, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testDeleteValueNotFound() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW + 1);
        when(paramValue.getNext()).thenReturn(null);
        crud.delete(NOW, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue);
        inOrder.verify(callback, times(1)).onException(any(IllegalArgumentException.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testDelete() {
        when(series.getLastValue()).thenReturn(paramValue);
        when(paramValue.getTimeStamp()).thenReturn(NOW);
        crud.delete(NOW, callback);
        inOrder.verify(series, times(1)).getLastValue();
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).removeValue(paramValue);
        inOrder.verify(callback, times(1)).onValue(paramValue);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#find(java.lang.Long, org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindNoValues() {
        assertNull(crud.find(series, NOW, null));
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#find(java.lang.Long, org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindOneValueNoMatch() {
        when(paramValue.getTimeStamp()).thenReturn(NOW + 1);
        when(paramValue.getNext()).thenReturn(null);
        assertNull(crud.find(series, NOW, paramValue));
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#find(java.lang.Long, org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindOneValueMatch() {
        when(paramValue.getTimeStamp()).thenReturn(NOW);
        assertEquals(paramValue, crud.find(series, NOW, paramValue));
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#find(java.lang.Long, org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindTwoValueNoMatch() {
        when(paramValue.getTimeStamp()).thenReturn(NOW + 2);
        when(series.next(paramValue)).thenReturn(paramValue2);
        when(paramValue2.getTimeStamp()).thenReturn(NOW + 1);
        when(series.next(paramValue2)).thenReturn(null);
        assertNull(crud.find(series, NOW, paramValue));
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue);
        inOrder.verify(paramValue2, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue2);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#find(java.lang.Long, org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testFindTwoValueMatch() {
        when(paramValue.getTimeStamp()).thenReturn(NOW + 1);
        when(series.next(paramValue)).thenReturn(paramValue2);
        when(paramValue2.getTimeStamp()).thenReturn(NOW);
        assertEquals(paramValue2, crud.find(series, NOW, paramValue));
        inOrder.verify(paramValue, times(1)).getTimeStamp();
        inOrder.verify(series, times(1)).next(paramValue);
        inOrder.verify(paramValue2, times(1)).getTimeStamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCrudSupport#handleException(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testHandleException() {
        crud.handleException(NOW, callback);
        ArgumentCaptor<IllegalArgumentException> captor = ArgumentCaptor.forClass(IllegalArgumentException.class);
        inOrder.verify(callback, times(1)).onException(captor.capture());
        inOrder.verifyNoMoreInteractions();
        assertTrue(captor.getValue().getMessage().contains(String.valueOf(NOW)));
        assertNull(captor.getValue().getCause());
    }
}
