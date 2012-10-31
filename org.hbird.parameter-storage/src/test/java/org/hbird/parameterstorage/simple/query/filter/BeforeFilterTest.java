package org.hbird.parameterstorage.simple.query.filter;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.query.filter.BeforeFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeforeFilterTest {

    public static final Long NOW = System.currentTimeMillis();

    @Mock
    private ParameterValueInTime<Object> value;

    private BeforeFilter<Object> beforeFilter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        beforeFilter = new BeforeFilter<Object>(NOW);
        inOrder = inOrder(value);
    }

    /**
     * Test method for {@link org.hbird.parameterstorage.simple.query.filter.BeforeFilter#BeforeFilter(java.lang.Long)}.
     */
    @Test
    public void testBeforeFilter() {
        testAccept();
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.BeforeFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAccept() {
        when(value.getTimeStamp()).thenReturn(NOW - 1, NOW, NOW + 1);
        assertTrue(beforeFilter.accept(value));
        assertFalse(beforeFilter.accept(value));
        assertFalse(beforeFilter.accept(value));
        inOrder.verify(value, times(3)).getTimeStamp();
        inOrder.verifyNoMoreInteractions();
    }
}
