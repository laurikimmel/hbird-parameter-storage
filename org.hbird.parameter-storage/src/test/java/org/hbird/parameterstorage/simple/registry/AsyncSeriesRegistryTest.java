package org.hbird.parameterstorage.simple.registry;

import java.util.concurrent.ExecutorService;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;
import org.hbird.parameterstorage.simple.registry.AsyncSeriesRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncSeriesRegistryTest {

    private static final String ID = "C";

    @Mock
    private ExecutorService execService;

    @Mock
    private SeriesRegistry<String, Object> registry;

    @Mock
    private CallbackWithValue<ParameterValueSeries<Object>> callback;

    private AsyncSeriesRegistry<String, Object> asyncSeriesRegistry;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        asyncSeriesRegistry = new AsyncSeriesRegistry<String, Object>(registry, execService);
        inOrder = inOrder(execService, registry, callback);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.AsyncSeriesRegistry#getOrCreate(java.lang.String, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGetOrCreate() {
        asyncSeriesRegistry.getOrCreate(ID, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.AsyncSeriesRegistry#remove(java.lang.String, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testRemove() {
        asyncSeriesRegistry.remove(ID, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.AsyncSeriesRegistry#createGetOrCreateTask(org.hbird.parameterstorage.api.registry.SeriesRegistry, java.lang.String, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCreateGetOrCreateTask() {
        Runnable r = asyncSeriesRegistry.createGetOrCreateTask(registry, ID, callback);
        r.run();
        inOrder.verify(registry, times(1)).getOrCreate(ID, callback);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.AsyncSeriesRegistry#createRemoveTask(org.hbird.parameterstorage.api.registry.SeriesRegistry, java.lang.String, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCreateRemoveTask() {
        Runnable r = asyncSeriesRegistry.createRemoveTask(registry, ID, callback);
        r.run();
        inOrder.verify(registry, times(1)).remove(ID, callback);
        inOrder.verifyNoMoreInteractions();
    }
}
