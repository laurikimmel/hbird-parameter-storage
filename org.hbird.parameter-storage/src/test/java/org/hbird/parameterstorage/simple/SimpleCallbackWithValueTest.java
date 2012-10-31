package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.simple.SimpleCallbackWithValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleCallbackWithValueTest {

    private final String MESSAGE = "Message";

    @Mock
    private Throwable throwable;

    @Mock
    private Object value;

    @Mock
    private SimpleCallbackWithValue<Object> mockCallback;

    private InOrder inOrder;

    private SimpleCallbackWithValue<Object> callback;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        callback = new SimpleCallbackWithValue<Object>();
        inOrder = inOrder(throwable, value, mockCallback);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleCallbackWithValue#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertNull(callback.getValue());
        callback.onValue(value);
        assertEquals(value, callback.getValue());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleCallbackWithValue#getThrowable()}.
     */
    @Test
    public void testGetThrowable() {
        assertNull(callback.getThrowable());
        callback.onException(throwable);
        assertEquals(throwable, callback.getThrowable());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleCallbackWithValue#onValue(java.lang.Object)}.
     */
    @Test
    public void testOnValue() {
        testGetValue();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleCallbackWithValue#onException(java.lang.Throwable)}
     * .
     */
    @Test
    public void testOnException() {
        testGetThrowable();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCallbackWithValue#verifyCallbackException(org.hbird.parameterstorage.simple.SimpleCallbackWithValue, java.lang.String)}
     * .
     */
    @Test
    public void testVerifyCallbackExceptionWithoutException() throws Exception {
        when(mockCallback.getThrowable()).thenReturn(null);
        SimpleCallbackWithValue.verifyCallbackException(mockCallback, MESSAGE);
        inOrder.verify(mockCallback, times(1)).getThrowable();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleCallbackWithValue#verifyCallbackException(org.hbird.parameterstorage.simple.SimpleCallbackWithValue, java.lang.String)}
     * .
     */
    @Test
    public void testVerifyCallbackExceptionWithException() throws Exception {
        when(mockCallback.getThrowable()).thenReturn(throwable);
        try {
            SimpleCallbackWithValue.verifyCallbackException(mockCallback, MESSAGE);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(throwable, e.getCause());
        }
        inOrder.verify(mockCallback, times(2)).getThrowable();
        inOrder.verifyNoMoreInteractions();
    }
}
