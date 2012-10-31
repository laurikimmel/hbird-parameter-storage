package org.hbird.parameterstorage.simple.query;

import java.util.concurrent.ExecutorService;

import org.hbird.parameterstorage.api.query.QueryCallback;
import org.hbird.parameterstorage.api.query.QuerySupport;
import org.hbird.parameterstorage.api.query.filter.Filter;


public class AsyncQuerySupport<T> implements QuerySupport<T> {

    private final QuerySupport<T> query;
    private final ExecutorService execService;

    /**
     * Creates new AsyncQuerySupport.
     * 
     * @param query
     * @param execService
     */
    public AsyncQuerySupport(QuerySupport<T> query, ExecutorService execService) {
        this.query = query;
        this.execService = execService;
    }

    /**
     * Returns query.
     * 
     * @return the query
     */
    public QuerySupport<T> getQuery() {
        return query;
    }

    /**
     * Returns execService.
     * 
     * @return the execService
     */
    public ExecutorService getExecService() {
        return execService;
    }

    /** @{inheritDoc . */
    @Override
    public void get(Filter<T> filter, QueryCallback<T> callback) {
        execService.submit(createGetTask(query, filter, callback));
    }

    /** @{inheritDoc . */
    @Override
    public void getLatest(Filter<T> filter, QueryCallback<T> callback) {
        execService.submit(createGetLatestTask(query, filter, callback));
    }

    /** @{inheritDoc . */
    @Override
    public void getAfter(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        execService.submit(createGetAfterTask(query, filter, callback, timeStamp));
    }

    /** @{inheritDoc . */
    @Override
    public void getLatestAfter(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        execService.submit(createGetLatestAfterTask(query, filter, callback, timeStamp));
    }

    /** @{inheritDoc . */
    @Override
    public void getBefore(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        execService.submit(createGetBeforeTask(query, filter, callback, timeStamp));
    }

    /** @{inheritDoc . */
    @Override
    public void getLatestBefore(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        execService.submit(createGetLatestBeforeTask(query, filter, callback, timeStamp));
    }

    /** @{inheritDoc . */
    @Override
    public void getBetween(Filter<T> filter, QueryCallback<T> callback, Long after, Long before) {
        execService.submit(createGetBetweenTask(query, filter, callback, after, before));
    }

    /** @{inheritDoc . */
    @Override
    public void getLatestBetween(Filter<T> filter, QueryCallback<T> callback, Long after, Long before) {
        execService.submit(createGetLatestBetweenTask(query, filter, callback, after, before));
    }

    protected Runnable createGetTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback) {
        return new Runnable() {
            @Override
            public void run() {
                query.get(filter, callback);
            }
        };
    }

    protected Runnable createGetLatestTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback) {
        return new Runnable() {
            @Override
            public void run() {
                query.getLatest(filter, callback);
            }
        };
    }

    protected Runnable createGetAfterTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback, final Long timeStamp) {
        return new Runnable() {
            @Override
            public void run() {
                query.getAfter(filter, callback, timeStamp);
            }
        };
    }

    protected Runnable createGetLatestAfterTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback, final Long timeStamp) {
        return new Runnable() {
            @Override
            public void run() {
                query.getLatestAfter(filter, callback, timeStamp);
            }
        };
    }

    protected Runnable createGetBeforeTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback, final Long timeStamp) {
        return new Runnable() {
            @Override
            public void run() {
                query.getBefore(filter, callback, timeStamp);
            }
        };
    }

    protected Runnable createGetLatestBeforeTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback, final Long timeStamp) {
        return new Runnable() {
            @Override
            public void run() {
                query.getLatestBefore(filter, callback, timeStamp);
            }
        };
    }

    protected Runnable createGetBetweenTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback, final Long after, final Long before) {
        return new Runnable() {
            @Override
            public void run() {
                query.getBetween(filter, callback, after, before);
            }
        };
    }

    protected Runnable createGetLatestBetweenTask(final QuerySupport<T> query, final Filter<T> filter,
            final QueryCallback<T> callback, final Long after, final Long before) {
        return new Runnable() {
            @Override
            public void run() {
                query.getLatestBetween(filter, callback, after, before);
            }
        };
    }
}
