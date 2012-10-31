package org.hbird.parameterstorage.simple;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SimpleParameterValueInTimeFactoryTest {

    private static final Long NOW = System.currentTimeMillis();
    private static final Long VALUE = 4324123L;

    private SimpleParameterValueInTimeFactory<Long> factory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new SimpleParameterValueInTimeFactory<Long>();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory#create(java.lang.Long, java.lang.Object)}
     * .
     */
    @Test
    public void testCreateNoDefinition() {
        factory = new SimpleParameterValueInTimeFactory<Long>();
        ParameterValueInTime<Long> value = factory.create(NOW, VALUE);
        assertNotNull(value);
        assertEquals(NOW, value.getTimeStamp());
        assertEquals(VALUE, value.getValue());
        assertNull(value.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory#create(java.lang.Long, java.lang.Object)}
     * .
     */
    @Test
    public void testCreateNoLimits() {
        ParameterValueInTime<Long> value = factory.create(NOW, VALUE);
        assertNotNull(value);
        assertEquals(NOW, value.getTimeStamp());
        assertEquals(VALUE, value.getValue());
        assertNull(value.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory#create(java.lang.Long, java.lang.Object)}
     * .
     */
    @Test
    public void testCreate() {
        ParameterValueInTime<Long> value = factory.create(NOW, VALUE);
        assertNotNull(value);
        assertEquals(NOW, value.getTimeStamp());
        assertEquals(VALUE, value.getValue());
        assertNull(value.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTimeFactory#createNew(Object, Long)} .
     */
    @Test
    public void testCreateNew() {
        ParameterValueInTime<Long> value1 = factory.createNew(VALUE, NOW);
        ParameterValueInTime<Long> value2 = factory.createNew(VALUE, NOW);
        assertNotNull(value1);
        assertNotNull(value2);
        assertNotSame(value1, value2);
        assertEquals(value1.getValue(), value2.getValue());
        assertEquals(value1.getTimeStamp(), value2.getTimeStamp());
        assertNull(value1.getNext());
        assertNull(value2.getNext());
    }
}
