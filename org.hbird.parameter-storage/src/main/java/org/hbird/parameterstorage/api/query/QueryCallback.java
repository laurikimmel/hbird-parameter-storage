package org.hbird.parameterstorage.api.query;

import org.hbird.parameterstorage.api.ParameterValueInTime;

public interface QueryCallback<T> {

    public void onStart();

    public void onValue(ParameterValueInTime<T> value);

    public void onEnd();

    public void onException(Throwable t);
}
