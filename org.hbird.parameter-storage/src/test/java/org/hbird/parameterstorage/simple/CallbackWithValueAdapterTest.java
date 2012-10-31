package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.simple.CallbackWithValueAdapter;
import org.junit.Before;
import org.junit.Test;

public class CallbackWithValueAdapterTest {

    private CallbackWithValue<Object> callback;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        callback = new CallbackWithValueAdapter<Object>();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.CallbackWithValueAdapter#onValue(java.lang.Object)}.
     */
    @Test
    public void testOnValue() {
        callback.onValue(new Object());
        callback.onValue(null);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.CallbackWithValueAdapter#onException(java.lang.Throwable)}.
     */
    @Test
    public void testOnException() {
        callback.onException(new Exception());
        callback.onException(null);
    }
}
