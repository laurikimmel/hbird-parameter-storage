package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.CrudSupport;
import org.hbird.parameterstorage.api.ParameterValueInTime;

public class SyncCrudSupport<T> {

    protected final CrudSupport<T> crud;

    public SyncCrudSupport(CrudSupport<T> crud) {
        this.crud = crud;
    }

    /**
     * Returns crud.
     * 
     * @return the crud
     */
    public CrudSupport<T> getCrud() {
        return crud;
    }

    public ParameterValueInTime<T> add(Long timestamp, T value) throws Exception {
        SimpleCallbackWithValue<ParameterValueInTime<T>> callback = new SimpleCallbackWithValue<ParameterValueInTime<T>>();
        crud.add(timestamp, value, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                String.format("Failed to add %s at %s.", value, timestamp));
        return callback.getValue();
    }

    public ParameterValueInTime<T> get(Long timestamp) throws Exception {
        SimpleCallbackWithValue<ParameterValueInTime<T>> callback = new SimpleCallbackWithValue<ParameterValueInTime<T>>();
        crud.get(timestamp, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                String.format("Failed to read value from %s", timestamp));
        return callback.getValue();
    }

    public ParameterValueInTime<T> update(Long timestamp, T value) throws Exception {
        SimpleCallbackWithValue<ParameterValueInTime<T>> callback = new SimpleCallbackWithValue<ParameterValueInTime<T>>();
        crud.update(timestamp, value, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                String.format("Failed to update value from %s", timestamp));
        return callback.getValue();
    }

    public ParameterValueInTime<T> delete(Long timestamp) throws Exception {
        SimpleCallbackWithValue<ParameterValueInTime<T>> callback = new SimpleCallbackWithValue<ParameterValueInTime<T>>();
        crud.delete(timestamp, callback);
        SimpleCallbackWithValue.verifyCallbackException(callback,
                String.format("Failed to delete value from %s", timestamp));
        return callback.getValue();
    }
}
