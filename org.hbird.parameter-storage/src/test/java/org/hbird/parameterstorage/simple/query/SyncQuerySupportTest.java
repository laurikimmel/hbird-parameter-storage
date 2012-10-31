package org.hbird.parameterstorage.simple.query;

import java.util.Arrays;
import java.util.List;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.QueryCallback;
import org.hbird.parameterstorage.api.query.QuerySupport;
import org.hbird.parameterstorage.api.query.filter.Filter;
import org.hbird.parameterstorage.simple.query.ListQueryCallbackAdapter;
import org.hbird.parameterstorage.simple.query.QueryCounterCallback;
import org.hbird.parameterstorage.simple.query.SyncQuerySupport;
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
public class SyncQuerySupportTest {

    private static final Long NOW = System.currentTimeMillis();

    @Mock
    private QuerySupport<Object> query;

    @Mock
    private ParameterValueInTime<Object> value1;

    @Mock
    private ParameterValueInTime<Object> value2;

    @Mock
    private ParameterValueInTime<Object> value3;

    @Mock
    private Filter<Object> filter;

    private ParameterValueInTime<Object>[] values;

    private SyncQuerySupport<Object> syncQuery;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        syncQuery = new SyncQuerySupport<Object>(query);
        values = (ParameterValueInTime<Object>[]) Arrays.asList(value1, value2, value3).toArray();
        inOrder = inOrder(query, value1, value2, value3, filter);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#SyncQuerySupport(org.hbird.parameterstorage.api.query.QuerySupport)}
     * .
     */
    @Test
    public void testSyncQuerySupport() {
        testGetQuery();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getQuery()}.
     */
    @Test
    public void testGetQuery() {
        assertEquals(query, syncQuery.getQuery());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#count(org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCount() {
        doAnswer(returnList).when(query).get(eq(filter), any(QueryCounterCallback.class));
        assertEquals(values.length, syncQuery.count(filter));
        inOrder.verify(query, times(1)).get(eq(filter), any(QueryCounterCallback.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#get(org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGet() {
        doAnswer(returnList).when(query).get(eq(filter), any(ListQueryCallbackAdapter.class));
        validateList(values, syncQuery.get(filter));
        inOrder.verify(query, times(1)).get(eq(filter), any(ListQueryCallbackAdapter.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getLatest(org.hbird.parameterstorage.api.query.filter.Filter)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetLatest() {
        doAnswer(returnFirst).when(query).getLatest(eq(filter), any(ListQueryCallbackAdapter.class));
        assertEquals(values[0], syncQuery.getLatest(filter));
        inOrder.verify(query, times(1)).getLatest(eq(filter), any(ListQueryCallbackAdapter.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getAfter(org.hbird.parameterstorage.api.query.filter.Filter, java.lang.Long)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetAfter() {
        doAnswer(returnList).when(query).getAfter(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        validateList(values, syncQuery.getAfter(filter, NOW));
        inOrder.verify(query, times(1)).getAfter(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getLatestAfter(org.hbird.parameterstorage.api.query.filter.Filter, java.lang.Long)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetLatestAfter() {
        doAnswer(returnFirst).when(query).getLatestAfter(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        assertEquals(values[0], syncQuery.getLatestAfter(filter, NOW));
        inOrder.verify(query, times(1)).getLatestAfter(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getBefore(org.hbird.parameterstorage.api.query.filter.Filter, java.lang.Long)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetBefore() {
        doAnswer(returnList).when(query).getBefore(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        validateList(values, syncQuery.getBefore(filter, NOW));
        inOrder.verify(query, times(1)).getBefore(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getLatestBefore(org.hbird.parameterstorage.api.query.filter.Filter, java.lang.Long)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetLatestBefore() {
        doAnswer(returnFirst).when(query).getLatestBefore(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        assertEquals(values[0], syncQuery.getLatestBefore(filter, NOW));
        inOrder.verify(query, times(1)).getLatestBefore(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getBetween(org.hbird.parameterstorage.api.query.filter.Filter, java.lang.Long, java.lang.Long)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetBetween() {
        doAnswer(returnList).when(query).getBetween(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW),
                eq(NOW + 100));
        validateList(values, syncQuery.getBetween(filter, NOW, NOW + 100));
        inOrder.verify(query, times(1)).getBetween(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW),
                eq(NOW + 100));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.SyncQuerySupport#getLatestBetween(org.hbird.parameterstorage.api.query.filter.Filter, java.lang.Long, java.lang.Long)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetLatestBetween() {
        doAnswer(returnFirst).when(query).getLatestBetween(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW),
                eq(NOW + 100));
        assertEquals(values[0], syncQuery.getLatestBetween(filter, NOW, NOW + 100));
        inOrder.verify(query, times(1)).getLatestBetween(eq(filter), any(ListQueryCallbackAdapter.class), eq(NOW),
                eq(NOW + 100));
        inOrder.verifyNoMoreInteractions();
    }

    private void validateList(ParameterValueInTime<Object>[] values, List<ParameterValueInTime<Object>> list) {
        assertNotNull(list);
        assertEquals(values.length, list.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], list.get(i));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final Answer returnList = new Answer() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            QueryCallback<Object> callback = (QueryCallback<Object>) invocation.getArguments()[1];
            for (ParameterValueInTime<Object> value : values) {
                callback.onValue(value);
            }
            return null;
        }
    };

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private final Answer returnFirst = new Answer() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            QueryCallback<Object> callback = (QueryCallback<Object>) invocation.getArguments()[1];
            callback.onValue(values[0]);
            return null;
        }
    };
}
