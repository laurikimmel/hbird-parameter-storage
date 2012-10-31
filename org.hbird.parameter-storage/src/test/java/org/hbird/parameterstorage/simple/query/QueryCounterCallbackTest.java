package org.hbird.parameterstorage.simple.query;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.query.QueryCounterCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class QueryCounterCallbackTest {

    @Mock
    private ParameterValueInTime<Object> value;

    private QueryCounterCallback<Object> counter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        counter = new QueryCounterCallback<Object>();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.QueryCounterCallback#onValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testOnValue() {
        for (int i = 0; i < 100; i++) {
            counter.onValue(value);
            assertEquals(i + 1, counter.getCount());
        }
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.QueryCounterCallback#getCount()}.
     */
    @Test
    public void testGetCount() {
        assertEquals(0, counter.getCount());
        testOnValue();
    }
}
