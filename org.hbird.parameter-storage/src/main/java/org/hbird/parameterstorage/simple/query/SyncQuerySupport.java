package org.hbird.parameterstorage.simple.query;

import java.util.List;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.QuerySupport;
import org.hbird.parameterstorage.api.query.filter.Filter;


public class SyncQuerySupport<T> {

    private final QuerySupport<T> query;

    /**
     * Creates new SyncQuerySupport.
     * 
     * @param query
     */
    public SyncQuerySupport(QuerySupport<T> query) {
        this.query = query;
    }

    /**
     * Returns query.
     * 
     * @return the query
     */
    public QuerySupport<T> getQuery() {
        return query;
    }

    public long count(Filter<T> filter) {
        QueryCounterCallback<T> counter = new QueryCounterCallback<T>();
        query.get(filter, counter);
        return counter.getCount();
    }

    public List<ParameterValueInTime<T>> get(Filter<T> filter) {
        ListQueryCallbackAdapter<T> adapter = new ListQueryCallbackAdapter<T>();
        query.get(filter, adapter);
        return adapter.getList();
    }

    public ParameterValueInTime<T> getLatest(Filter<T> filter) {
        GetLatestQueryCallbackAdapter<T> callback = new GetLatestQueryCallbackAdapter<T>();
        query.getLatest(filter, callback);
        return callback.getValue();
    }

    public List<ParameterValueInTime<T>> getAfter(Filter<T> filter, Long timeStamp) {
        ListQueryCallbackAdapter<T> adapter = new ListQueryCallbackAdapter<T>();
        query.getAfter(filter, adapter, timeStamp);
        return adapter.getList();
    }

    public ParameterValueInTime<T> getLatestAfter(Filter<T> filter, Long timeStamp) {
        GetLatestQueryCallbackAdapter<T> callback = new GetLatestQueryCallbackAdapter<T>();
        query.getLatestAfter(filter, callback, timeStamp);
        return callback.getValue();
    }

    public List<ParameterValueInTime<T>> getBefore(Filter<T> filter, Long timeStamp) {
        ListQueryCallbackAdapter<T> adapter = new ListQueryCallbackAdapter<T>();
        query.getBefore(filter, adapter, timeStamp);
        return adapter.getList();
    }

    public ParameterValueInTime<T> getLatestBefore(Filter<T> filter, Long timeStamp) {
        GetLatestQueryCallbackAdapter<T> callback = new GetLatestQueryCallbackAdapter<T>();
        query.getLatestBefore(filter, callback, timeStamp);
        return callback.getValue();
    }

    public List<ParameterValueInTime<T>> getBetween(Filter<T> filter, Long after, Long before) {
        ListQueryCallbackAdapter<T> adapter = new ListQueryCallbackAdapter<T>();
        query.getBetween(filter, adapter, after, before);
        return adapter.getList();
    }

    public ParameterValueInTime<T> getLatestBetween(Filter<T> filter, Long after, Long before) {
        GetLatestQueryCallbackAdapter<T> callback = new GetLatestQueryCallbackAdapter<T>();
        query.getLatestBetween(filter, callback, after, before);
        return callback.getValue();
    }
}
