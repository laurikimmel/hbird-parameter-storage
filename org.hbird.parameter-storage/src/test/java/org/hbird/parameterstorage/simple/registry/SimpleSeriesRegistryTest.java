package org.hbird.parameterstorage.simple.registry;

import java.util.HashMap;
import java.util.Map;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesFactory;
import org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleSeriesRegistryTest {

    private static final String ID_1 = "A";
    private static final String ID_2 = "B";

    @Mock
    private SeriesFactory<Object> factory;

    @Mock
    private ParameterValueSeries<Object> s1;

    @Mock
    private ParameterValueSeries<Object> s2;

    @Mock
    private CallbackWithValue<ParameterValueSeries<Object>> callback;

    @Mock
    private Map<String, ParameterValueSeries<Object>> map;

    private InOrder inOrder;

    private SimpleSeriesRegistry<String, Object> registry;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        registry = new SimpleSeriesRegistry<String, Object>(factory);
        when(factory.create()).thenReturn(s1, s2);
        inOrder = inOrder(factory, s1, s2, callback, map);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry#SimpleSeriesRegistry(org.hbird.parameterstorage.api.registry.SeriesFactory)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleSeriesRegistryWithNullFactory() {
        new SimpleSeriesRegistry<String, Object>(null);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry#getOrCreate(java.lang.String, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGetOrCreate() {
        registry.getOrCreate(ID_1, callback);
        registry.getOrCreate(ID_2, callback);
        registry.getOrCreate(ID_1, callback);
        registry.getOrCreate(ID_2, callback);

        inOrder.verify(factory, times(1)).create();
        inOrder.verify(callback, times(1)).onValue(s1);
        inOrder.verify(factory, times(1)).create();
        inOrder.verify(callback, times(1)).onValue(s2);
        inOrder.verify(callback, times(1)).onValue(s1);
        inOrder.verify(callback, times(1)).onValue(s2);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry#remove(java.lang.String, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testRemove() {
        registry.getOrCreate(ID_1, callback);
        registry.remove(ID_1, callback);
        registry.remove(ID_1, callback);
        registry.remove(ID_2, callback);
        registry.getOrCreate(ID_1, callback);
        registry.remove(ID_1, callback);
        registry.remove(ID_1, callback);
        registry.remove(ID_2, callback);

        inOrder.verify(factory, times(1)).create();
        inOrder.verify(callback, times(2)).onValue(s1);
        inOrder.verify(callback, times(2)).onValue(null);
        inOrder.verify(factory, times(1)).create();
        inOrder.verify(callback, times(2)).onValue(s2);
        inOrder.verify(callback, times(2)).onValue(null);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry#create(java.lang.String, org.hbird.parameterstorage.api.registry.SeriesFactory, java.util.Map)}
     * .
     */
    @Test
    public void testCreate() {
        assertEquals(s1, registry.create(ID_1, factory, map));
        assertEquals(s2, registry.create(ID_1, factory, map));

        inOrder.verify(factory, times(1)).create();
        inOrder.verify(map, times(1)).put(ID_1, s1);
        inOrder.verify(factory, times(1)).create();
        inOrder.verify(map, times(1)).put(ID_1, s2);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.registry.SimpleSeriesRegistry#createRegistry()} .
     */
    @Test
    public void testRegistry() {
        Map<String, ParameterValueSeries<Object>> map1 = registry.createRegistry();
        Map<String, ParameterValueSeries<Object>> map2 = registry.createRegistry();
        assertNotNull(map1);
        assertNotNull(map2);
        assertNotSame(map1, map2);
        assertEquals(0, map1.size());
        assertEquals(0, map2.size());
        assertEquals(HashMap.class, map1.getClass());
        assertEquals(HashMap.class, map2.getClass());
    }
}
