package org.hbird.parameterstorage.concurrent;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime;
import org.hbird.parameterstorage.concurrent.AtomicParameterValueInTimeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtomicParameterValueInTimeFactoryTest {

    private static final Long NOW = System.currentTimeMillis();
    private static final Object VALUE = new Object();

    private AtomicParameterValueInTimeFactory<Object> factory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new AtomicParameterValueInTimeFactory<Object>();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTimeFactory#createNew(java.lang.Object, java.lang.Long)}
     * .
     */
    @Test
    public void testCreateNew() {
        ParameterValueInTime<Object> value1 = factory.createNew(VALUE, NOW);
        ParameterValueInTime<Object> value2 = factory.createNew(VALUE, NOW);

        assertNotNull(value1);
        assertEquals(VALUE, value1.getValue());
        assertEquals(NOW, value1.getTimeStamp());
        assertNull(value1.getNext());
        assertEquals(AtomicParameterValueInTime.class, value1.getClass());
        AtomicParameterValueInTime<Object> val1 = (AtomicParameterValueInTime<Object>) value1;
        assertFalse(val1.isMarkedAsDeleted());

        assertNotNull(value2);
        assertEquals(VALUE, value2.getValue());
        assertEquals(NOW, value2.getTimeStamp());
        assertNull(value2.getNext());
        assertEquals(AtomicParameterValueInTime.class, value2.getClass());
        AtomicParameterValueInTime<Object> val2 = (AtomicParameterValueInTime<Object>) value1;
        assertFalse(val2.isMarkedAsDeleted());

        assertNotSame(value1, value2);
    }
}
