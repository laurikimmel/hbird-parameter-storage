package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;

public class SimpleParameterValueSeries<T> implements ParameterValueSeries<T> {

    protected ParameterValueInTime<T> lastValue;

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> getLastValue() {
        return lastValue;
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> addValue(ParameterValueInTime<T> value) throws IllegalArgumentException {
        ParameterValueInTime<T> addedTo = addValue(lastValue, value);
        if (addedTo == null) {
            throw new IllegalArgumentException(String.format("Timestamp %s already in series", value.getTimeStamp()));
        }
        if (addedTo.getTimeStamp().equals(value.getTimeStamp())) {
            lastValue = value;
            return lastValue;
        }
        return lastValue;
    }

    protected ParameterValueInTime<T> addValue(ParameterValueInTime<T> head, ParameterValueInTime<T> newValue) {

        if (head == null) {
            return newValue;
        }

        ParameterValueInTime<T> curr = head;
        ParameterValueInTime<T> prev = null;

        while (true) {
            if (curr == null) {
                // last value; newValue is added to the end
                return bind(prev, curr, newValue);
            }

            if (curr.getTimeStamp() < newValue.getTimeStamp()) {
                // found place to add newValue
                return bind(prev, curr, newValue);
            } else if (curr.getTimeStamp().equals(newValue.getTimeStamp())) {
                // no duplicate entries
                return null;
            } else {
                // check the next value
                ParameterValueInTime<T> tail = curr.getNext();
                prev = curr;
                curr = tail;
            }
        }
    }

    protected ParameterValueInTime<T> bind(ParameterValueInTime<T> head, ParameterValueInTime<T> tail,
            ParameterValueInTime<T> newValue) {
        if (tail != null) {
            newValue.setNext(tail);
        }
        if (head != null) {
            head.setNext(newValue);
            return head;
        } else {
            return newValue;
        }
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> removeValue(ParameterValueInTime<T> value) throws IllegalArgumentException {
        if (lastValue == null) {
            // empty series; nothing to remove
            // TODO - 23.02.2012 kimmell - throw IllegalArgumentException
            return null;
        }

        ParameterValueInTime<T> updated = null;
        if (value.getTimeStamp().equals(lastValue.getTimeStamp())) {
            // remove from the first position
            updated = lastValue;
            // TODO - 16.02.2012 kimmell - synchronize?
            lastValue = updated.getNext();
            updated.setNext(null);
            return lastValue;
        } else {
            // remove from somewhere below
            updated = lastValue.getNext() == null ? null : removeValue(lastValue, value);
            if (updated == null) {
                throw new IllegalArgumentException(String.format("No entry found for timestamp %s",
                        value.getTimeStamp()));
            } else {
                return updated;
            }
        }
    }

    protected ParameterValueInTime<T> removeValue(ParameterValueInTime<T> head, ParameterValueInTime<T> toRemove) {
        if (head == null) {
            return toRemove;
        }

        ParameterValueInTime<T> curr = head;
        ParameterValueInTime<T> prev = null;
        ParameterValueInTime<T> result = null;

        boolean flag = true;

        while (flag) {
            if (curr.getTimeStamp().equals(toRemove.getTimeStamp())) {
                ParameterValueInTime<T> next = curr.getNext();
                if (prev != null) {
                    prev.setNext(next);
                    toRemove.setNext(null);
                    return prev;
                } else {
                    toRemove.setNext(null);
                    return next;
                }
            } else {
                prev = curr;
                curr = curr.getNext();
                if (curr == null) {
                    return null;
                }
            }
        }

        return result;
    }

    /** @{inheritDoc . */
    @Override
    public void count(CallbackWithValue<Long> callback) {
        long counter = 0;
        ParameterValueInTime<T> current = lastValue;
        while (current != null) {
            counter++;
            current = current.getNext();
        }
        callback.onValue(new Long(counter));
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> next(ParameterValueInTime<T> value) {
        return value.getNext();
    }
}
