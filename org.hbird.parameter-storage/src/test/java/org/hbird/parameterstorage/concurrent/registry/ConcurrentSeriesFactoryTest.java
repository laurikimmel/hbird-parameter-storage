package org.hbird.parameterstorage.concurrent.registry;


import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.concurrent.ConcurrentParameterValueSeries;
import org.hbird.parameterstorage.concurrent.registry.ConcurrentSeriesFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrentSeriesFactoryTest {

    private ConcurrentSeriesFactory<Object> factory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new ConcurrentSeriesFactory<Object>();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.registry.ConcurrentSeriesFactory#create()}.
     */
    @Test
    public void testCreate() {
        ParameterValueSeries<Object> s1 = factory.create();
        ParameterValueSeries<Object> s2 = factory.create();
        assertNotNull(s1);
        assertNotNull(s2);
        assertNotSame(s1, s2);
        assertEquals(ConcurrentParameterValueSeries.class, s1.getClass());
        assertEquals(ConcurrentParameterValueSeries.class, s2.getClass());
    }
}
