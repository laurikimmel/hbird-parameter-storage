package org.hbird.parameterstorage.simple.query;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;
import org.hbird.parameterstorage.api.query.QueryCallback;
import org.hbird.parameterstorage.api.query.QuerySupport;
import org.hbird.parameterstorage.api.query.filter.Filter;

import static org.hbird.parameterstorage.simple.query.filter.Filters.*;

public class SimpleQuerySupport<T> implements QuerySupport<T> {

    private final ParameterValueSeries<T> series;

    public SimpleQuerySupport(ParameterValueSeries<T> series) {
        this.series = series;
    }

    /** @{inheritDoc . */
    @Override
    public void get(Filter<T> filter, QueryCallback<T> callback) {
        try {
            callback.onStart();
            Filter<T> acceptAll = acceptAll();
            select(series, filter, callback, acceptAll);
            callback.onEnd();
        } catch (Throwable t) {
            callback.onException(t);
        }
    }

    /** @{inheritDoc . */
    @Override
    public void getLatest(Filter<T> filter, QueryCallback<T> callback) {
        try {
            callback.onStart();
            Filter<T> acceptAll = acceptAll();
            selectFirst(series, filter, callback, acceptAll);
            callback.onEnd();
        } catch (Throwable t) {
            callback.onException(t);
        }
    }

    /** @{inheritDoc . */
    @Override
    public void getAfter(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        try {
            callback.onStart();
            Filter<T> stopAfter = stopAfter(timeStamp);
            select(series, filter, callback, stopAfter);
            callback.onEnd();
        } catch (Throwable t) {
            callback.onException(t);
        }
    }

    /** @{inheritDoc . */
    @Override
    public void getLatestAfter(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        try {
            callback.onStart();
            Filter<T> stopAfter = stopAfter(timeStamp);
            selectFirst(series, filter, callback, stopAfter);
            callback.onEnd();
        } catch (Throwable t) {
            callback.onException(t);
        }
    }

    /** @{inheritDoc . */
    @Override
    public void getBefore(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        Filter<T> before = before(timeStamp);
        @SuppressWarnings("unchecked")
        Filter<T> compoundFilter = and(filter, before);
        get(compoundFilter, callback);
    }

    /** @{inheritDoc . */
    @Override
    public void getLatestBefore(Filter<T> filter, QueryCallback<T> callback, Long timeStamp) {
        Filter<T> before = before(timeStamp);
        @SuppressWarnings("unchecked")
        Filter<T> compoundFilter = and(filter, before);
        getLatest(compoundFilter, callback);
    }

    /** @{inheritDoc . */
    @Override
    public void getBetween(Filter<T> filter, QueryCallback<T> callback, Long after, Long before) {
        Filter<T> beforeFilter = before(before);
        @SuppressWarnings("unchecked")
        Filter<T> compoundFilter = and(filter, beforeFilter);
        getAfter(compoundFilter, callback, after);
    }

    /** @{inheritDoc . */
    @Override
    public void getLatestBetween(Filter<T> filter, QueryCallback<T> callback, Long after, Long before) {
        Filter<T> beforeFilter = before(before);
        @SuppressWarnings("unchecked")
        Filter<T> compoundFilter = and(filter, beforeFilter);
        getLatestAfter(compoundFilter, callback, after);
    }

    protected void select(ParameterValueSeries<T> series, Filter<T> filter, QueryCallback<T> callback,
            Filter<T> continueFilter) {

        // 22.02.2012 kimmell - can't use recursion here. Will run out of java call stack ~8381 elements =|
        ParameterValueInTime<T> element = series.getLastValue();
        if (element == null) {
            return;
        }

        while (true) {
            if (filter.accept(element)) {
                callback.onValue(element);
            }
            if (!continueFilter.accept(element)) {
                return;
            }
            element = series.next(element);
            if (element == null) {
                return;
            }
        }
    }

    protected void selectFirst(ParameterValueSeries<T> series, Filter<T> filter, QueryCallback<T> callback,
            Filter<T> continueFilter) {

        // 22.02.2012 kimmell - can't use recursion here. Will run out of java call stack ~8381 elements =|
        ParameterValueInTime<T> element = series.getLastValue();

        if (element == null) {
            return;
        }

        while (true) {
            if (filter.accept(element)) {
                callback.onValue(element);
                return;
            }
            if (!continueFilter.accept(element)) {
                return;
            }
            element = series.next(element);
            if (element == null) {
                return;
            }
        }
    }
}
