package org.hbird.parameterstorage.simple;

import java.util.concurrent.ExecutorService;


import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.AsyncCrudSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Matchers.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncCrudSupportTest {

    private static final Long NOW = System.currentTimeMillis();
    private static final Object VALUE = new Object();

    @Mock
    private CrudSupport<Object> crud;

    @Mock
    private ExecutorService execService;

    @Mock
    private CallbackWithValue<ParameterValueInTime<Object>> callback;

    private AsyncCrudSupport<Object> asyncCrudSupport;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        asyncCrudSupport = new AsyncCrudSupport<Object>(crud, execService);
        inOrder = inOrder(crud, execService, callback);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#AsyncCrudSupport(org.hbird.parameterstorage.api.CrudSupport, java.util.concurrent.ExecutorService)}
     * .
     */
    @Test
    public void testAsyncCrudSupport() {
        testGetCrud();
        testGetExecService();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#getCrud()}.
     */
    @Test
    public void testGetCrud() {
        assertEquals(crud, asyncCrudSupport.getCrud());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#getExecService()}.
     */
    @Test
    public void testGetExecService() {
        assertEquals(execService, asyncCrudSupport.getExecService());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#add(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testAdd() {
        asyncCrudSupport.add(NOW, VALUE, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#get(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testGet() {
        asyncCrudSupport.get(NOW, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#update(java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testUpdate() {
        asyncCrudSupport.update(NOW, VALUE, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#delete(java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testDelete() {
        asyncCrudSupport.delete(NOW, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#createAddTask(org.hbird.parameterstorage.api.CrudSupport, java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCreateAddTask() {
        Runnable r = asyncCrudSupport.createAddTask(crud, NOW, VALUE, callback);
        r.run();
        inOrder.verify(crud, times(1)).add(NOW, VALUE, callback);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#createGetTask(org.hbird.parameterstorage.api.CrudSupport, java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCreateGetTask() {
        Runnable r = asyncCrudSupport.createGetTask(crud, NOW, callback);
        r.run();
        inOrder.verify(crud, times(1)).get(NOW, callback);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#createUpdateTask(org.hbird.parameterstorage.api.CrudSupport, java.lang.Long, java.lang.Object, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCreateUpdateTask() {
        Runnable r = asyncCrudSupport.createUpdateTask(crud, NOW, VALUE, callback);
        r.run();
        inOrder.verify(crud, times(1)).update(NOW, VALUE, callback);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.AsyncCrudSupport#createDeleteTask(org.hbird.parameterstorage.api.CrudSupport, java.lang.Long, org.hbird.parameterstorage.api.CallbackWithValue)}
     * .
     */
    @Test
    public void testCreateDeleteTask() {
        Runnable r = asyncCrudSupport.createDeleteTask(crud, NOW, callback);
        r.run();
        inOrder.verify(crud, times(1)).delete(NOW, callback);
        inOrder.verifyNoMoreInteractions();
    }
}
