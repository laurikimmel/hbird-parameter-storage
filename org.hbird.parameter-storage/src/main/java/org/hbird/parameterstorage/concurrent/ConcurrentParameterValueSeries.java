package org.hbird.parameterstorage.concurrent;

import org.hbird.parameterstorage.api.CallbackWithValue;
import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.api.ParameterValueSeries;

/**
 * Atomic lock free ParameterValueSeries implementation based on http://www.boyet.com/Articles/LockFreeLinkedList3.html
 * Suitable for concurrent environments.
 */
public class ConcurrentParameterValueSeries<T> implements ParameterValueSeries<T> {

    protected final AtomicParameterValueInTime<T> head;

    /**
     * Creates new ConcurrentParameterValueSeries.
     * 
     * @param definition
     */
    public ConcurrentParameterValueSeries() {
        head = createHead();
    }

    protected AtomicParameterValueInTime<T> createHead() {
        return new AtomicParameterValueInTime<T>(null, Long.MIN_VALUE);
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> getLastValue() {
        return head.getNext();
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> addValue(ParameterValueInTime<T> value) {
        AtomicParameterValueInTime<T> parent;
        AtomicParameterValueInTime<T> oldValue;
        AtomicParameterValueInTime<T> newValue = (AtomicParameterValueInTime<T>) value;
        do {
            parent = findParentForAdd(newValue);
            if (parent == null) {
                throw new IllegalArgumentException(
                        String.format("Timestamp %s already in series", value.getTimeStamp()));
            }
            oldValue = (AtomicParameterValueInTime<T>) parent.getNext();
        } while (!insert(parent, oldValue, newValue));
        return parent == head ? null : parent;
//        return parent;
    }

    boolean insert(AtomicParameterValueInTime<T> parent, AtomicParameterValueInTime<T> oldChild,
            AtomicParameterValueInTime<T> newChild) {
        newChild.setNext(oldChild);
        boolean flag = parent.tryToSetNext(oldChild, newChild);
        if (!flag) {
            // insert failed - revert changes in new child
            newChild.setNext(null);
        }
        return flag;
    }

    AtomicParameterValueInTime<T> findParentForAdd(ParameterValueInTime<T> value) {
        AtomicParameterValueInTime<T> parent = head;
        AtomicParameterValueInTime<T> child = (AtomicParameterValueInTime<T>) parent.getNext();
        while (child != null) {
            if (child.getTimeStamp().equals(value.getTimeStamp())) {
                // no duplicates
                return null;
            } else if (child.getTimeStamp() < value.getTimeStamp()) {
                return parent;
            } else {
                parent = child;
                child = (AtomicParameterValueInTime<T>) child.getNext();
            }
        }
        return parent;
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> removeValue(ParameterValueInTime<T> value) {
        AtomicParameterValueInTime<T> parent = findParentForRemove(value);
        if (parent == null) {
            throw new IllegalArgumentException(String.format("No entry found for timestamp %s", value.getTimeStamp()));
        } else {
            AtomicParameterValueInTime<T> toDelete = (AtomicParameterValueInTime<T>) parent.getNext();
            toDelete.markAsDeleted();
            return parent == head ? null : parent;
//            return parent;
        }
    }

    AtomicParameterValueInTime<T> findParentForRemove(ParameterValueInTime<T> value) {
        AtomicParameterValueInTime<T> parent = head;
        AtomicParameterValueInTime<T> child = (AtomicParameterValueInTime<T>) head.getNext();
        while (child != null) {
            if (child.getTimeStamp() < value.getTimeStamp()) {
                return null;
            } else if (child.getTimeStamp().equals(value.getTimeStamp())) {
                return parent;
            } else {
                parent = child;
                child = (AtomicParameterValueInTime<T>) child.getNext();
            }
        }
        return null;
    }

    /** @{inheritDoc . */
    @Override
    public void count(CallbackWithValue<Long> callback) {
        long counter = 0;
        AtomicParameterValueInTime<T> current = (AtomicParameterValueInTime<T>) head.getNext();
        while (current != null) {
            counter++;
            current = (AtomicParameterValueInTime<T>) current.getNext();
        }
        callback.onValue(counter);
    }

    /** @{inheritDoc . */
    @Override
    public ParameterValueInTime<T> next(ParameterValueInTime<T> value) {
        return value.getNext();
    }
}
