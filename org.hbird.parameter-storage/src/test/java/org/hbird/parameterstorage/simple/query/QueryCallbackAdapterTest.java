package org.hbird.parameterstorage.simple.query;


import org.hbird.parameterstorage.simple.SimpleParameterValueInTime;
import org.hbird.parameterstorage.simple.query.QueryCallbackAdapter;
import org.junit.Before;
import org.junit.Test;

public class QueryCallbackAdapterTest {

    private QueryCallbackAdapter<Object> callback;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        callback = new QueryCallbackAdapter<Object>();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.QueryCallbackAdapter#onStart()}.
     */
    @Test
    public void testOnStart() {
        callback.onStart();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.QueryCallbackAdapter#onValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testOnValue() {
        callback.onValue(new SimpleParameterValueInTime<Object>(new Object(), System.currentTimeMillis()));
        callback.onValue(null);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.QueryCallbackAdapter#onEnd()}.
     */
    @Test
    public void testOnEnd() {
        callback.onEnd();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.QueryCallbackAdapter#onException(java.lang.Throwable)}.
     */
    @Test
    public void testOnException() {
        callback.onException(new Exception());
        callback.onException(null);
    }
}
