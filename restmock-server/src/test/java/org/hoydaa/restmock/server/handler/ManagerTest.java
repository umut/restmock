package org.hoydaa.restmock.server.handler;

import static junit.framework.TestCase.*;

import org.hoydaa.restmock.client.Method;
import org.hoydaa.restmock.client.Server;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.easymock.EasyMock.*;

/**
 * @author Umut Utkan
 */
public class ManagerTest {

    private Server server;

    private Manager manager;

    private HttpServletRequest request;

    private HttpServletResponse response;


    @Before
    public void before() {
        server = new Server();
        manager = new MockManager();
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
    }

    @Test
    public void shouldTestForExpectations() throws IOException {
        expect(request.getPathInfo()).andReturn("/api/service").anyTimes();
        replay(request);

        manager.handle(request, response);

        assertEquals(Manager.SC_EXPECTATIONS_NOT_SET, MockManager.code);
    }

    @Test
    public void shouldTestForPath() throws IOException {
        server.expect("/api/service1", Method.GET);
        Manager.server = server;

        expect(request.getPathInfo()).andReturn("/api/service").anyTimes();
        replay(request);

        manager.handle(request, response);

        assertEquals(Manager.SC_NOT_EXPECTED_PATH, MockManager.code);

        verify(request);
    }

    @Test
    public void shouldTestForMethod() throws IOException {
        server.expect("/api/service1", Method.GET);
        Manager.server = server;

        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.POST.name()).anyTimes();
        replay(request);

        manager.handle(request, response);

        assertEquals(Manager.SC_WRONG_METHOD, MockManager.code);

        verify(request);
    }

    @Test
    public void shouldTestForParameterMismatch() {
        server.expect("/api/service1", Method.GET).withParam("param1", new String[]{"value1"});
        Manager.server = server;

        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        replay(request);

        verify(request);
    }

    @Test
    public void shouldTestForHeaderMismatch() {

    }

    public static class MockManager extends Manager {

        private static int code = 0;

        @Override
        protected void sendResponse(HttpServletRequest request, HttpServletResponse response, String error, int code) throws IOException {
            MockManager.code = code;
        }
    }

}
