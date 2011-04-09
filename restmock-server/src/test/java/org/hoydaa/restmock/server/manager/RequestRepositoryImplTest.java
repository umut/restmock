package org.hoydaa.restmock.server.manager;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.model.Method;
import org.hoydaa.restmock.model.Request;
import org.hoydaa.restmock.model.Response;
import org.hoydaa.restmock.model.Server;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.easymock.EasyMock.*;

/**
 * @author Umut Utkan
 */
public class RequestRepositoryImplTest {

    @Test
    public void shouldReturnMockRequestProperly() throws IOException {
        Server server = new Server();
        server.setUrl("http://localhost:8989");

        Response res1 = new Response();
        res1.setType("application/json");
        res1.setStatus(200);
        res1.setStream(IOUtils.toInputStream("{}"));
        Request req1 = new Request();
        req1.setPath("/service1");
        req1.setMethod(Method.GET);
        req1.setResponse(res1);
        server.getRequests().add(req1);

        Response res2 = new Response();
        res2.setType("application/json");
        res2.setStatus(200);
        res2.setStream(IOUtils.toInputStream("{}"));
        Request req2 = new Request();
        req2.setPath("/service2");
        req2.setMethod(Method.GET);
        req2.setResponse(res2);
        req2.setTimes(2);
        server.getRequests().add(req2);

        RequestRepositoryImpl requestRepository = new RequestRepositoryImpl();
        requestRepository.setServer(server);

        HttpServletRequest service1 = createMock(HttpServletRequest.class);
        expect(service1.getPathInfo()).andReturn("/service1").anyTimes();
        expect(service1.getMethod()).andReturn("GET").anyTimes();
        replay(service1);

        Request request1 = requestRepository.getRequest(service1);
        assertNotNull(request1);
        try {
            request1 = requestRepository.getRequest(service1);
            fail("Shoud have thrown " + RequestRepositoryException.class.getName());
        } catch (RequestRepositoryException e) {

        }
        verify(service1);

        HttpServletRequest service2 = createMock(HttpServletRequest.class);
        expect(service2.getPathInfo()).andReturn("/service2").anyTimes();
        expect(service2.getMethod()).andReturn("GET").anyTimes();
        replay(service2);
        Request request2 = null;
        for (int i = 0; i < 2; i++) {
            request2 = requestRepository.getRequest(service2);
            assertNotNull(request2);
        }
        try {
            request2 = requestRepository.getRequest(service2);
            fail("Shoud have thrown " + RequestRepositoryException.class.getName());
        } catch (RequestRepositoryException e) {

        }
        verify(service2);
    }

}
