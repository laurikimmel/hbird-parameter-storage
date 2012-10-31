package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.CallbackWithValue;

/**
 * @author kimmell
 */
public class SimpleCallbackWithValue<T> implements CallbackWithValue<T> {

    protected T value;
    protected Throwable throwable;

    /**
     * Returns value.
     * 
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns throwable.
     * 
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /** @{inheritDoc . */
    @Override
    public void onValue(T value) {
        this.value = value;
    }

    /** @{inheritDoc . */
    @Override
    public void onException(Throwable throwable) {
        this.throwable = throwable;
    }

    public static <T> void verifyCallbackException(SimpleCallbackWithValue<T> callback, String message)
            throws Exception {
        if (callback.getThrowable() != null) {
            throw new Exception(message, callback.getThrowable());
        }
    }

}
