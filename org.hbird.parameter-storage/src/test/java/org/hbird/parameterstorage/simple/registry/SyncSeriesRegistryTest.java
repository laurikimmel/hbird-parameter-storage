package org.hbird.parameterstorage.simple.registry;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.registry.SeriesRegistry;
import org.hbird.parameterstorage.simple.SimpleCallbackWithValue;
import org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SyncSeriesRegistryTest {

    private static final String ID = "D";

    @Mock
    private SeriesRegistry<String, Object> registry;

    @Mock
    private ParameterValueSeries<Object> series;

    @Mock
    private Exception exception;

    private InOrder inOrder;

    private SyncSeriesRegistry<String, Object> syncSeriesRegistry;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        syncSeriesRegistry = new SyncSeriesRegistry<String, Object>(registry);
        inOrder = inOrder(registry, series, exception);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry#getRegistry()}.
     */
    @Test
    public void testGetRegistry() {
        assertEquals(registry, syncSeriesRegistry.getRegistry());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry#getOrGreate(java.lang.String)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetOrGreate() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onValue(series);
                return null;
            }
        }).when(registry).getOrCreate(eq(ID), any(SimpleCallbackWithValue.class));

        assertEquals(series, syncSeriesRegistry.getOrGreate(ID));
        inOrder.verify(registry, times(1)).getOrCreate(eq(ID), any(SimpleCallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry#getOrGreate(java.lang.String)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetOrGreateWithException() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onException(exception);
                return null;
            }
        }).when(registry).getOrCreate(eq(ID), any(SimpleCallbackWithValue.class));
        try {
            syncSeriesRegistry.getOrGreate(ID);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
        inOrder.verify(registry, times(1)).getOrCreate(eq(ID), any(SimpleCallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry#remove(java.lang.String)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemove() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onValue(series);
                return null;
            }
        }).when(registry).remove(eq(ID), any(SimpleCallbackWithValue.class));

        assertEquals(series, syncSeriesRegistry.remove(ID));
        inOrder.verify(registry, times(1)).remove(eq(ID), any(SimpleCallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.registry.SyncSeriesRegistry#remove(java.lang.String)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveWithException() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onException(exception);
                return null;
            }
        }).when(registry).remove(eq(ID), any(SimpleCallbackWithValue.class));

        try {
            syncSeriesRegistry.remove(ID);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
        inOrder.verify(registry, times(1)).remove(eq(ID), any(SimpleCallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }
}
