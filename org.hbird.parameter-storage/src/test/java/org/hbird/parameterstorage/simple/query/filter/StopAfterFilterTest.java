package org.hbird.parameterstorage.simple.query.filter;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.query.filter.StopAfterFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StopAfterFilterTest {

    private static final Long NOW = System.currentTimeMillis();

    @Mock
    private ParameterValueInTime<Object> value;

    private StopAfterFilter<Object> stopAfterFilter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        stopAfterFilter = new StopAfterFilter<Object>(NOW);
        inOrder = inOrder(value);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.StopAfterFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAccept() {
        when(value.getTimeStamp()).thenReturn(NOW - 1, NOW, NOW + 1);
        assertFalse(stopAfterFilter.accept(value));
        assertTrue(stopAfterFilter.accept(value));
        assertTrue(stopAfterFilter.accept(value));
        inOrder.verify(value, times(3)).getTimeStamp();
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.StopAfterFilter#StopAfterFilter(java.lang.Long)}.
     */
    @Test
    public void testStopAfterFilter() {
        testAccept();
    }
}
