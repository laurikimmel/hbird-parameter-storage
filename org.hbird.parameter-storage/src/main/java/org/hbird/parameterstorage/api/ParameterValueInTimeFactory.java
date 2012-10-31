package org.hbird.parameterstorage.api;

public interface ParameterValueInTimeFactory<T> {

    public ParameterValueInTime<T> create(Long timeStamp, T value) throws IllegalArgumentException;

}
