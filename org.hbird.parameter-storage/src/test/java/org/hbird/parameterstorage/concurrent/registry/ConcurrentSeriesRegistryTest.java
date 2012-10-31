package org.hbird.parameterstorage.concurrent.registry;

import java.util.HashMap;
import java.util.Map;


import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.concurrent.registry.ConcurrentSeriesRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConcurrentSeriesRegistryTest {

    @Mock
    private SeriesFactory<Object> factory;

    private InOrder inOrder;

    private ConcurrentSeriesRegistry<Object, Object> registry;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        registry = new ConcurrentSeriesRegistry<Object, Object>(factory);
        inOrder = inOrder(factory);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.registry.ConcurrentSeriesRegistry#createRegistry()}.
     */
    @Test
    public void testCreateRegistry() {
        Map<Object, ParameterValueSeries<Object>> map1 = registry.createRegistry();
        Map<Object, ParameterValueSeries<Object>> map2 = registry.createRegistry();
        assertNotNull(map1);
        assertNotNull(map2);
        assertNotSame(map1, map2);
        assertEquals(0, map1.size());
        assertEquals(0, map2.size());
        assertEquals(HashMap.class, map1.getClass());
        assertEquals(HashMap.class, map2.getClass());
        inOrder.verifyNoMoreInteractions();
    }
}
