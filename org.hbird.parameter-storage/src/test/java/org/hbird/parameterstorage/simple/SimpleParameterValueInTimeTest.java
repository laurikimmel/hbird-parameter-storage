package org.hbird.parameterstorage.simple;

import org.hbird.parameterstorage.simple.SimpleParameterValueInTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleParameterValueInTimeTest {

    private static final Long NOW = System.currentTimeMillis();
    private static final Long TIMESTAMP_1 = NOW + 1L;
    private static final Long TIMESTAMP_2 = NOW + 2L;
    private static final Long VALUE_1 = 10101L;
    private static final Long VALUE_2 = 20202L;

    private SimpleParameterValueInTime<Long> value1;
    private SimpleParameterValueInTime<Long> value2;
    private SimpleParameterValueInTime<Long> value3;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        value1 = new SimpleParameterValueInTime<Long>(VALUE_1, TIMESTAMP_1);
        value2 = new SimpleParameterValueInTime<Long>(VALUE_2, TIMESTAMP_2);
        value3 = new SimpleParameterValueInTime<Long>(null, null);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#SimpleParameterValueInTime(java.lang.Object, java.lang.Long)}
     * .
     */
    @Test
    public void testSimpleParameterValueInTimeTLong() {
        assertEquals(VALUE_1, value1.getValue());
        assertEquals(TIMESTAMP_1, value1.getTimeStamp());
        assertNull(value1.getNext());
        assertEquals(VALUE_2, value2.getValue());
        assertEquals(TIMESTAMP_2, value2.getTimeStamp());
        assertNull(value2.getNext());
        assertNull(value3.getValue());
        assertNull(value3.getTimeStamp());
        assertNull(value3.getNext());
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#SimpleParameterValueInTime(java.lang.Object, java.lang.Long, org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testSimpleParameterValueInTimeTLongParameterValueInTimeOfT() {
        value1 = new SimpleParameterValueInTime<Long>(VALUE_1, TIMESTAMP_1, null);
        value2 = new SimpleParameterValueInTime<Long>(VALUE_2, TIMESTAMP_2, value1);
        value3 = new SimpleParameterValueInTime<Long>(null, null, null);
        assertEquals(VALUE_1, value1.getValue());
        assertEquals(TIMESTAMP_1, value1.getTimeStamp());
        assertNull(value1.getNext());
        assertEquals(VALUE_2, value2.getValue());
        assertEquals(TIMESTAMP_2, value2.getTimeStamp());
        assertEquals(value1, value2.getNext());
        assertNull(value3.getValue());
        assertNull(value3.getTimeStamp());
        assertNull(value3.getNext());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertEquals(VALUE_1, value1.getValue());
        assertEquals(VALUE_2, value2.getValue());
        assertNull(value3.getValue());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#getTimeStamp()}.
     */
    @Test
    public void testGetTimeStamp() {
        assertEquals(TIMESTAMP_1, value1.getTimeStamp());
        assertEquals(TIMESTAMP_2, value2.getTimeStamp());
        assertNull(value3.getTimeStamp());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#getNext()}.
     */
    @Test
    public void testGetNext() {
        testSetNext();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#setNext(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testSetNext() {
        assertNull(value1.getNext());
        assertNull(value2.getNext());
        assertNull(value3.getNext());
        value1.setNext(value2);
        value2.setNext(value3);
        value3.setNext(value1);
        assertEquals(value2, value1.getNext());
        assertEquals(value3, value2.getNext());
        assertEquals(value1, value3.getNext());
        value1.setNext(null);
        value2.setNext(null);
        value3.setNext(null);
        assertNull(value1.getNext());
        assertNull(value2.getNext());
        assertNull(value3.getNext());

        value1.setNext(value2);
        assertEquals(value2, value1.getNext());
        assertNull(value2.getNext());
        assertNull(value3.getNext());
        value1.setNext(value3);
        assertEquals(value3, value1.getNext());
        assertNull(value2.getNext());
        assertNull(value3.getNext());
        value1.setNext(null);
        assertNull(value1.getNext());
        assertNull(value2.getNext());
        assertNull(value3.getNext());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.SimpleParameterValueInTime#toString()}.
     */
    @Test
    public void testToString() {
        assertNotNull(value1.toString());
        assertNotNull(value2.toString());
    }
}
