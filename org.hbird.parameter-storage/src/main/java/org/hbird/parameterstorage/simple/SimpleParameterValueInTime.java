package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.ParameterValueInTime;

public class SimpleParameterValueInTime<T> implements ParameterValueInTime<T> {

    protected final T value;
    protected final Long timeStamp;
    protected ParameterValueInTime<T> nextValue;

    /**
     * Creates new SimpleParameterValueInTime.
     * 
     * @param value
     * @param timeStamp
     * @param nextValue
     */
    public SimpleParameterValueInTime(T value, Long timeStamp) {
        this(value, timeStamp, null);
    }

    /**
     * Creates new SimpleParameterValueInTime.
     * 
     * @param value
     * @param timeStamp
     * @param nextValue
     */
    public SimpleParameterValueInTime(T value, Long timeStamp, ParameterValueInTime<T> nextValue) {
        this.value = value;
        this.timeStamp = timeStamp;
        this.nextValue = nextValue;
    }

    /** @{inheritDoc . */
    @Override
    public T getValue() {
        return value;
    }

    /** @{inheritDoc . */
    @Override
    public Long getTimeStamp() {
        return timeStamp;
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> getNext() {
        return nextValue;
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> setNext(ParameterValueInTime<T> next) {
        ParameterValueInTime<T> lastNext = nextValue;
        nextValue = next;
        return lastNext;
    }

    /** @{inheritDoc . */
    @Override
    public String toString() {
        return "SimpleParameterValueInTime [value=" + value + ", timeStamp=" + timeStamp + ", nextValue="
                + (nextValue == null ? "null" : nextValue.hashCode()) + "]";
    }
}
