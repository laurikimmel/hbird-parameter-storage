package org.hbird.parameterstorage.simple.query;

import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.query.QueryCallback;

public class QueryCallbackAdapter<T> implements QueryCallback<T> {

    /** @{inheritDoc . */
    @Override
    public void onStart() {
    }

    /** @{inheritDoc . */
    @Override
    public void onValue(ParameterValueInTime<T> value) {
    }

    /** @{inheritDoc . */
    @Override
    public void onEnd() {
    }

    /** @{inheritDoc . */
    @Override
    public void onException(Throwable t) {
    }
}
