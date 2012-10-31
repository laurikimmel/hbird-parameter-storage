package org.hbird.parameterstorage.simple.query.filter;


import org.hbird.parameterstorage.api.ParameterValueInTime;
import org.hbird.parameterstorage.simple.query.filter.AcceptAllFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AcceptAllFilterTest {

    @Mock
    private ParameterValueInTime<Double> value;

    private AcceptAllFilter<Double> filter;

    private InOrder inOrder;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        filter = new AcceptAllFilter<Double>();
        inOrder = inOrder(value);
    }

    /**
     * Test method for
     * {@link org.hbird.parameterstorage.simple.query.filter.AcceptAllFilter#accept(org.hbird.parameterstorage.api.ParameterValueInTime)}
     * .
     */
    @Test
    public void testAccept() {
        assertTrue(filter.accept(null));
        assertTrue(filter.accept(value));
        inOrder.verifyNoMoreInteractions();
    }
}
