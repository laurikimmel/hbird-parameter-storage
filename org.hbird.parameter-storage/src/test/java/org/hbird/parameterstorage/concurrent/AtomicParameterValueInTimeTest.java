package org.hbird.parameterstorage.concurrent;


import org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AtomicParameterValueInTimeTest {

    private static final Long NOW = System.currentTimeMillis();

    @Mock
    private AtomicParameterValueInTime<Object> next1;

    @Mock
    private AtomicParameterValueInTime<Object> next2;

    @Mock
    private Object value;

    private AtomicParameterValueInTime<Object> apvit;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        apvit = new AtomicParameterValueInTime<Object>(value, NOW);
        inOrder = inOrder(next1, next2, value);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#AtomicParameterValueInTime(java.lang.Object, java.lang.Long)}
     * .
     */
    @Test
    public void testAtomicParameterValueInTime() {
        assertEquals(NOW, apvit.getTimeStamp());
        assertEquals(value, apvit.getValue());
        assertNull(apvit.getNext());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertEquals(value, apvit.getValue());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#getTimeStamp()}.
     */
    @Test
    public void testGetTimeStamp() {
        assertEquals(NOW, apvit.getTimeStamp());
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#getNext()}.
     */
    @Test
    public void testGetNext() {
        testSetNext();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#setNext(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testSetNext() {
        when(next1.isMarkedAsDeleted()).thenReturn(false);
        when(next2.isMarkedAsDeleted()).thenReturn(false);
        assertNull(apvit.getNext());
        apvit.setNext(next1);
        assertEquals(next1, apvit.getNext());
        apvit.setNext(next1);
        assertEquals(next1, apvit.getNext());
        apvit.setNext(next2);
        assertEquals(next2, apvit.getNext());
        apvit.setNext(next1);
        assertEquals(next1, apvit.getNext());
        apvit.setNext(next2);
        assertEquals(next2, apvit.getNext());
        inOrder.verify(next1, times(2)).isMarkedAsDeleted();
        inOrder.verify(next2, times(1)).isMarkedAsDeleted();
        inOrder.verify(next1, times(1)).isMarkedAsDeleted();
        inOrder.verify(next2, times(1)).isMarkedAsDeleted();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#isMarkedAsDeleted()} .
     */
    @Test
    public void testIsMarkedForDelete() {
        testMarkAsDeleted();
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.concurrent.AtomicParameterValueInTime#markAsDeleted()} .
     */
    @Test
    public void testMarkAsDeleted() {
        assertFalse(apvit.isMarkedAsDeleted());
        apvit.markAsDeleted();
        assertTrue(apvit.isMarkedAsDeleted());
    }
}
