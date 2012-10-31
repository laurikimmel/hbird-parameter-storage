package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueInTimeFactory;
import org.hbird.parameterstorage.api.ParameterValueSeries;

public class SimpleCrudSupport<T> implements CrudSupport<T> {

    protected final ParameterValueInTimeFactory<T> factory;
    protected final ParameterValueSeries<T> series;

    public SimpleCrudSupport(ParameterValueSeries<T> series, ParameterValueInTimeFactory<T> factory) {
        this.series = series;
        this.factory = factory;
    }

    /** @{inheritDoc . */
    @Override
    public void add(Long timeStamp, T value, CallbackWithValue<ParameterValueInTime<T>> callback) {
        try {
            ParameterValueInTime<T> newValue = factory.create(timeStamp, value);
            series.addValue(newValue);
            callback.onValue(newValue);
        } catch (Throwable e) {
            callback.onException(e);
        }
    }

    /** @{inheritDoc . */
    @Override
    public void get(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback) {
        ParameterValueInTime<T> result = null;
        result = find(series, timeStamp, series.getLastValue());
        if (result == null) {
            handleException(timeStamp, callback);
        } else {
            callback.onValue(result);
        }
    }

    /** @{inheritDoc . */
    @Override
    public void update(Long timeStamp, T value, CallbackWithValue<ParameterValueInTime<T>> callback) {
        ParameterValueInTime<T> oldValue = null;
        oldValue = find(series, timeStamp, series.getLastValue());

        if (oldValue == null) {
            handleException(timeStamp, callback);
        } else {
            try {
                ParameterValueInTime<T> newValue = factory.create(timeStamp, value);
                series.removeValue(oldValue);
                series.addValue(newValue);
                callback.onValue(newValue);
            } catch (IllegalArgumentException e) {
                callback.onException(e);
            }
        }
    }

    /** @{inheritDoc . */
    @Override
    public void delete(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback) {
        ParameterValueInTime<T> toRemove = null;
        toRemove = find(series, timeStamp, series.getLastValue());
        if (toRemove == null) {
            handleException(timeStamp, callback);
        } else {
            series.removeValue(toRemove);
            callback.onValue(toRemove);
        }
    }

    protected ParameterValueInTime<T> find(ParameterValueSeries<T> series, Long timeStamp, ParameterValueInTime<T> head) {
        ParameterValueInTime<T> curr = head;
        while (true) {
            if (curr == null) {
                return null;
            }
            if (curr.getTimeStamp().equals(timeStamp)) {
                return curr;
            } else {
                curr = series.next(curr);
            }
        }
    }

    protected void handleException(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback)
            throws IllegalArgumentException {
        callback.onException(new IllegalArgumentException(String.format("Not found entry for timestamp %s", timeStamp)));
    }
}
