package org.hbird.parameterstorage.api.query;

import org.hbird.parameterstorage.api.query.filter.Filter;

public interface QuerySupport<T> {

    public void get(Filter<T> filter, QueryCallback<T> callback);

    public void getLatest(Filter<T> filter, QueryCallback<T> callback);

    public void getAfter(Filter<T> filter, QueryCallback<T> callback, Long timeStamp);

    public void getLatestAfter(Filter<T> filter, QueryCallback<T> callback, Long timeStamp);

    public void getBefore(Filter<T> filter, QueryCallback<T> callback, Long timeStamp);

    public void getLatestBefore(Filter<T> filter, QueryCallback<T> callback, Long timeStamp);

    public void getBetween(Filter<T> filter, QueryCallback<T> callback, Long after, Long before);

    public void getLatestBetween(Filter<T> filter, QueryCallback<T> callback, Long after, Long before);
}
