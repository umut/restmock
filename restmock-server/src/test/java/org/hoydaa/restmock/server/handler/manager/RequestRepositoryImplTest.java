package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.RestMock;
import org.hoydaa.restmock.client.internal.ServerControl;
import org.hoydaa.restmock.client.model.Method;
import org.hoydaa.restmock.client.model.Request;
import org.hoydaa.restmock.client.model.Server;
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
        ServerControl control = (ServerControl) RestMock.defineServer("http://localhost:8989")
                .expect("/service1", Method.GET).andReply("application/json", 200, "{}")
                .expect("/service2", Method.GET).andReply("application/json", 200, "{}").times(2);
        control.finish();

        RequestRepositoryImpl requestRepository = new RequestRepositoryImpl();
        requestRepository.setServer(control.getServer());

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
