package org.hbird.parameterstorage.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.hbird.parameterstorage.api.ParameterValueInTime;


public class AtomicParameterValueInTime<T> implements ParameterValueInTime<T> {

    protected T value;
    protected Long timestamp;
//    protected final AtomicMarkableReference<AtomicParameterValueInTime<T>> nextRef;
    protected final AtomicReference<AtomicParameterValueInTime<T>> nextRef;
    protected final AtomicBoolean deleted;

    public AtomicParameterValueInTime() {
        this(null, null);
    }

    /**
     * Creates new AtomicParameterValueInTime.
     * 
     * @param value
     * @param timestamp
     */
    public AtomicParameterValueInTime(T value, Long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
//        nextRef = new AtomicMarkableReference<AtomicParameterValueInTime<T>>(null, false);
        nextRef = new AtomicReference<AtomicParameterValueInTime<T>>(null);
        deleted = new AtomicBoolean(false);
    }

    /** @{inheritDoc . */
    @Override
    public T getValue() {
        return value;
    }

    /** @{inheritDoc . */
    @Override
    public Long getTimeStamp() {
        return timestamp;
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> getNext() {
        AtomicParameterValueInTime<T> parent = this;
//        AtomicParameterValueInTime<T> child = this.nextRef.getReference();
        AtomicParameterValueInTime<T> child = this.nextRef.get();
        while (child != null && child.isMarkedAsDeleted()) {
            tryToDeleteChild(child);
            parent = child;
//            child = parent.nextRef.getReference();
            child = parent.nextRef.get();
        }
        return child;
    }

    void tryToDeleteChild(AtomicParameterValueInTime<T> child) {
//        if (nextRef.getReference().equals(child)) {
        if (nextRef.get().equals(child)) {
//            AtomicParameterValueInTime<T> newChild = child.nextRef.getReference();
            AtomicParameterValueInTime<T> newChild = child.nextRef.get();
            tryToSetNext(child, newChild);
        }
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> setNext(ParameterValueInTime<T> newNext) {
        AtomicParameterValueInTime<T> lastValue;
        AtomicParameterValueInTime<T> newValue = (AtomicParameterValueInTime<T>) newNext;
//        boolean flag;
        do {
//            lastValue = nextRef.getReference();
//            flag = nextRef.isMarked();
            lastValue = nextRef.get();
            // } while (!nextRef.compareAndSet(lastValue, newValue, flag, flag));
        } while (!nextRef.compareAndSet(lastValue, newValue));
        return lastValue;
    }

    public boolean tryToSetNext(ParameterValueInTime<T> oldNext, ParameterValueInTime<T> newNext) {
        AtomicParameterValueInTime<T> oldValue = (AtomicParameterValueInTime<T>) oldNext;
        AtomicParameterValueInTime<T> newValue = (AtomicParameterValueInTime<T>) newNext;
//        boolean flag = nextRef.isMarked();
//        return nextRef.compareAndSet(oldValue, newValue, flag, flag);
        return nextRef.compareAndSet(oldValue, newValue);
    }

    public void markAsDeleted() {
        boolean flag = deleted.get();
        deleted.compareAndSet(flag, true);
//        AtomicParameterValueInTime<T> lastValue;
//        do {
//            lastValue = nextRef.getReference();
//        } while (!nextRef.attemptMark(lastValue, true));
    }

    public boolean isMarkedAsDeleted() {
        return deleted.get();
//        return nextRef.isMarked();
    }
}
