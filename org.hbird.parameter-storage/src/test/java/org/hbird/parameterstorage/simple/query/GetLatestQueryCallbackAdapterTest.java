package org.hbird.parameterstorage.simple.query;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.query.GetLatestQueryCallbackAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GetLatestQueryCallbackAdapterTest {

    @Mock
    private ParameterValueInTime<Object> value1;

    @Mock
    private ParameterValueInTime<Object> value2;

    private GetLatestQueryCallbackAdapter<Object> getLatest;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        getLatest = new GetLatestQueryCallbackAdapter<Object>();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.GetLatestQueryCallbackAdapter#onValue(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testOnValue() {
        getLatest.onValue(value1);
        assertEquals(value1, getLatest.getValue());
        getLatest.onValue(value2);
        assertEquals(value2, getLatest.getValue());
        getLatest.onValue(value1);
        assertEquals(value1, getLatest.getValue());
        getLatest.onValue(value2);
        assertEquals(value2, getLatest.getValue());
        getLatest.onValue(value2);
        assertEquals(value2, getLatest.getValue());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.GetLatestQueryCallbackAdapter#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertNull(getLatest.getValue());
        testOnValue();
    }
}
