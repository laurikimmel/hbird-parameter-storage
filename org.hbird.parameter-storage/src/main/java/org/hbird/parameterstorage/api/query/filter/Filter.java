package org.hbird.parameterstorage.api.query.filter;

import org.hbird.parameterstorage.api.ParameterValueInTime;

public interface Filter<T> {

    public boolean accept(ParameterValueInTime<T> value);
}
