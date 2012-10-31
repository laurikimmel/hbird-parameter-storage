package org.hbird.parameterstorage.api;

public interface CallbackWithValue<T> {

    public void onValue(T t);

    public void onException(Throwable t);
}
