package org.hbird.parameterstorage.api;

public interface ParameterValueSeries<T> {

    public ParameterValueInTime<T> getLastValue();

    public ParameterValueInTime<T> addValue(ParameterValueInTime<T> value);

    public ParameterValueInTime<T> removeValue(ParameterValueInTime<T> value);

    public ParameterValueInTime<T> next(ParameterValueInTime<T> value);

    public void count(CallbackWithValue<Long> callback);
}
