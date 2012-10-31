package org.hbird.parameterstorage.api;

public interface ParameterValueInTime<T> {

    public T getValue();

    public Long getTimeStamp();

    public ParameterValueInTime<T> getNext();

    public ParameterValueInTime<T> setNext(ParameterValueInTime<T> next);
}
