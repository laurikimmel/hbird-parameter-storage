package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.CallbackWithValue;

public class CallbackWithValueAdapter<T> implements CallbackWithValue<T> {

    /** @{inheritDoc . */
    @Override
    public void onValue(T t) {
    }

    /** @{inheritDoc . */
    @Override
    public void onException(Throwable t) {
    }
}
