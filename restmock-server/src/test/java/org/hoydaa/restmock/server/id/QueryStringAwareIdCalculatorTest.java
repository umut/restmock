package org.hoydaa.restmock.server.id;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static junit.framework.TestCase.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author Umut Utkan
 */
public class QueryStringAwareIdCalculatorTest {

    private HttpServletRequest request;

    private IdCalculator idCalculator;


    @Before
    public void before() {
        request = createMock(HttpServletRequest.class);
        idCalculator = new QueryStringAwareIdCalculator();
    }

    @Test
    public void shouldCalculateIdWithoutQueryString() {
        expect(request.getPathInfo()).andReturn("/path1/path2");
        expect(request.getQueryString()).andReturn(null).anyTimes();
        replay(request);

        assertEquals("/path1/path2", idCalculator.calculate(request));

        verify(request);
    }

    @Test
    public void shouldCalculateIdWithQueryString() {
        expect(request.getPathInfo()).andReturn("/path1/path2");
        expect(request.getQueryString()).andReturn("param1=val1&param2=val2").anyTimes();
        replay(request);

        assertEquals("/path1/path2_param1_val1_param2_val2", idCalculator.calculate(request));

        verify(request);
    }

}
