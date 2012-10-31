package org.hbird.parameterstorage.api;

public interface CrudSupport<T> {

    public void add(Long timeStamp, T value, CallbackWithValue<ParameterValueInTime<T>> callback);

    public void get(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback);

    public void update(Long timeStamp, T value, CallbackWithValue<ParameterValueInTime<T>> callback);

    public void delete(Long timeStamp, CallbackWithValue<ParameterValueInTime<T>> callback);
}
