package org.hbird.parameterstorage.simple.registry;


import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.simple.SimpleParameterValueSeries;
import org.hbird.parameterstorage.simple.registry.SimpleSeriesFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleSeriesFactoryTest {

    private SimpleSeriesFactory<Object> factory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new SimpleSeriesFactory<Object>();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.registry.SimpleSeriesFactory#create()}.
     */
    @Test
    public void testCreate() {
        ParameterValueSeries<Object> s1 = factory.create();
        ParameterValueSeries<Object> s2 = factory.create();
        assertNotNull(s1);
        assertNotNull(s2);
        assertEquals(SimpleParameterValueSeries.class, s1.getClass());
        assertEquals(SimpleParameterValueSeries.class, s2.getClass());
        assertNotSame(s1, s2);
        assertFalse(s1 == s2);
    }
}
