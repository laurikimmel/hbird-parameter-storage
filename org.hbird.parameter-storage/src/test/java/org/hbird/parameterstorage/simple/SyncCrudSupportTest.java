package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleCallbackWithValue;
import org.hbird.parameterstorage.simple.SyncCrudSupport;
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
public class SyncCrudSupportTest {

    private final Object VALUE = new Object();
    private final Long NOW = System.currentTimeMillis();

    @Mock
    private CrudSupport<Object> crud;

    @Mock
    private ParameterValueInTime<Object> result;

    @Mock
    private Exception exception;

    @Mock
    private SimpleCallbackWithValue<ParameterValueInTime<Object>> callback;

    private SyncCrudSupport<Object> syncCrud;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        syncCrud = new SyncCrudSupport<Object>(crud);
        inOrder = inOrder(crud, result, exception, callback);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SyncCrudSupport#SynchronousCrudSupport(org.hbird.parameterstorage.api.CrudSupport)}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testSynchronousCrudSupport() throws Exception {
        testGetCrud();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#getCrud()} .
     */
    @Test
    public void testGetCrud() {
        assertEquals(crud, syncCrud.getCrud());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#add(java.lang.Long, java.lang.Object)}.
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAdd() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[2];
                c.onValue(result);
                return null;
            }
        }).when(crud).add(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        assertEquals(result, syncCrud.add(NOW, VALUE));
        inOrder.verify(crud, times(1)).add(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#add(java.lang.Long, java.lang.Object)}.
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testAddWithException() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[2];
                c.onException(exception);
                return null;
            }
        }).when(crud).add(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        try {
            syncCrud.add(NOW, VALUE);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
        inOrder.verify(crud, times(1)).add(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#get(java.lang.Long)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGet() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onValue(result);
                return null;
            }
        }).when(crud).get(eq(NOW), any(CallbackWithValue.class));
        assertEquals(result, syncCrud.get(NOW));
        inOrder.verify(crud, times(1)).get(eq(NOW), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#get(java.lang.Long)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetWithException() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onException(exception);
                return null;
            }
        }).when(crud).get(eq(NOW), any(CallbackWithValue.class));
        try {
            syncCrud.get(NOW);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
        inOrder.verify(crud, times(1)).get(eq(NOW), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#update(java.lang.Long, java.lang.Object)}
     * .
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testUpdate() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[2];
                c.onValue(result);
                return null;
            }
        }).when(crud).update(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        assertEquals(result, syncCrud.update(NOW, VALUE));
        inOrder.verify(crud, times(1)).update(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#update(java.lang.Long, java.lang.Object)}
     * .
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateWithException() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[2];
                c.onException(exception);
                return null;
            }
        }).when(crud).update(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        try {
            syncCrud.update(NOW, VALUE);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
        inOrder.verify(crud, times(1)).update(eq(NOW), eq(VALUE), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#delete(java.lang.Long)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDelete() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onValue(result);
                return null;
            }
        }).when(crud).delete(eq(NOW), any(CallbackWithValue.class));
        assertEquals(result, syncCrud.delete(NOW));
        inOrder.verify(crud, times(1)).delete(eq(NOW), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SyncCrudSupport#delete(java.lang.Long)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteWithException() throws Exception {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CallbackWithValue<Object> c = (CallbackWithValue<Object>) invocation.getArguments()[1];
                c.onException(exception);
                return null;
            }
        }).when(crud).delete(eq(NOW), any(CallbackWithValue.class));
        try {
            syncCrud.delete(NOW);
            fail("Exception expected");
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
        inOrder.verify(crud, times(1)).delete(eq(NOW), any(CallbackWithValue.class));
        inOrder.verifyNoMoreInteractions();
    }

}
