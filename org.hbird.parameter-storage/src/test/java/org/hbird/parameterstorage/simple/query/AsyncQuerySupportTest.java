package org.hbird.parameterstorage.simple.query;

import java.util.concurrent.ExecutorService;


import org.hbird.parameterstorage.api.query.QueryCallback;
import org.hbird.parameterstorage.api.query.QuerySupport;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.query.AsyncQuerySupport;
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
public class AsyncQuerySupportTest {

    private static final Long NOW = System.currentTimeMillis();

    @Mock
    private ExecutorService execService;

    @Mock
    private QuerySupport<Object> query;

    @Mock
    private QueryCallback<Object> callback;

    @Mock
    private Filter<Object> filter;

    private AsyncQuerySupport<Object> asyncQuery;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        asyncQuery = new AsyncQuerySupport<Object>(query, execService);
        inOrder = inOrder(execService, query, callback, filter);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#AsyncQuerySupport(org.hbird.parameterstorage.api.query.QuerySupport, java.util.concurrent.ExecutorService)}
     * .
     */
    @Test
    public void testAsyncQuerySupport() {
        testGetQuery();
        testGetExecService();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getQuery()}.
     */
    @Test
    public void testGetQuery() {
        assertEquals(execService, asyncQuery.getExecService());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getExecService()}.
     */
    @Test
    public void testGetExecService() {
        assertEquals(query, asyncQuery.getQuery());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGet() {
        asyncQuery.get(filter, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testGetLatest() {
        asyncQuery.getLatest(filter, callback);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetAfter() {
        asyncQuery.getAfter(filter, callback, NOW);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestAfter() {
        asyncQuery.getLatestAfter(filter, callback, NOW);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBefore() {
        asyncQuery.getBefore(filter, callback, NOW);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBefore() {
        asyncQuery.getLatestBefore(filter, callback, NOW);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetBetween() {
        asyncQuery.getBetween(filter, callback, NOW, NOW + 1);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#getLatestBetween(org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testGetLatestBetween() {
        asyncQuery.getLatestBetween(filter, callback, NOW, NOW + 1);
        inOrder.verify(execService, times(1)).submit(any(Runnable.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testCreateGetTask() {
        Runnable r = asyncQuery.createGetTask(query, filter, callback);
        r.run();
        inOrder.verify(query, times(1)).get(filter, callback);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetLatestTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback)}
     * .
     */
    @Test
    public void testCreateGetLatestTask() {
        Runnable r = asyncQuery.createGetLatestTask(query, filter, callback);
        r.run();
        inOrder.verify(query, times(1)).getLatest(filter, callback);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetAfterTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateGetAfterTask() {
        Runnable r = asyncQuery.createGetAfterTask(query, filter, callback, NOW);
        r.run();
        inOrder.verify(query, times(1)).getAfter(filter, callback, NOW);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetLatestAfterTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateGetLatestAfterTask() {
        Runnable r = asyncQuery.createGetLatestAfterTask(query, filter, callback, NOW);
        r.run();
        inOrder.verify(query, times(1)).getLatestAfter(filter, callback, NOW);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetBeforeTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateGetBeforeTask() {
        Runnable r = asyncQuery.createGetBeforeTask(query, filter, callback, NOW);
        r.run();
        inOrder.verify(query, times(1)).getBefore(filter, callback, NOW);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetLatestBeforeTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateGetLatestBeforeTask() {
        Runnable r = asyncQuery.createGetLatestBeforeTask(query, filter, callback, NOW);
        r.run();
        inOrder.verify(query, times(1)).getLatestBefore(filter, callback, NOW);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetBetweenTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateGetBetweenTask() {
        Runnable r = asyncQuery.createGetBetweenTask(query, filter, callback, NOW, NOW + 1);
        r.run();
        inOrder.verify(query, times(1)).getBetween(filter, callback, NOW, NOW + 1);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.AsyncQuerySupport#createGetLatestBetweenTask(org.hbird.parameterstorage.api.query.QuerySupport, org.hbird.parameterstorage.api.query.filter.Filter, org.hbird.parameterstorage.api.query.QueryCallback, java.lang.Long, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateGetLatestBetweenTask() {
        Runnable r = asyncQuery.createGetLatestBetweenTask(query, filter, callback, NOW, NOW + 1);
        r.run();
        inOrder.verify(query, times(1)).getLatestBetween(filter, callback, NOW, NOW + 1);
        inOrder.verifyNoMoreInteractions();
    }
}
