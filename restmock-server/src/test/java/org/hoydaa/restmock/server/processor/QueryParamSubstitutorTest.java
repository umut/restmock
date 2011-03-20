package org.hoydaa.restmock.server.processor;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author Umut Utkan
 */
public class QueryParamSubstitutorTest {

    private PostProcessor processor;

    private HttpServletRequest request;


    @Before
    public void before() {
        request = createMock(HttpServletRequest.class);
        processor = new QueryParamSubstitutor();
    }

    @Test
    public void shouldSubstituteParameters() throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(
                "{{param1}} brown {{param2}} jumped over the blah blah blah!".getBytes());
        Map<String, String> params = new HashMap<String, String>();
        params.put("param1", "quick");
        params.put("param2", "fox");
        expect(request.getParameterMap()).andReturn(params);
        replay(request);

        assertEquals("quick brown fox jumped over the blah blah blah!",
                IOUtils.toString(processor.process(stream, request)));

        verify(request);
    }

}
