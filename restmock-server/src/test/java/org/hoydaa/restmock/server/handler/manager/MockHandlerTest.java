package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Method;
import org.hoydaa.restmock.client.Server;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author Umut Utkan
 */
public class MockHandlerTest {

    private RequestRepository requestRepository;

    private MockHandler mockHandler;

    private HttpServletRequest request;

    private HttpServletResponse response;


    @Before
    public void before() {
        requestRepository = createMock(RequestRepository.class);
        mockHandler = new MockedMockHandler();
        mockHandler.setRequestRepository(requestRepository);
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
    }

    @Test
    public void shouldTestForExpectations() throws IOException {
        expect(request.getPathInfo()).andReturn("/api/service").anyTimes();
        expect(requestRepository.isReady()).andReturn(false);
        replay(request, requestRepository);

        mockHandler.handle(request, response);

        assertEquals(MockHandler.SC_EXPECTATIONS_NOT_SET, MockedMockHandler.code);

        verify(request, requestRepository);
    }

    @Test
    public void shouldTestForExistence() throws IOException {
        expect(request.getPathInfo()).andReturn("/api/service").anyTimes();
        expect(request.getMethod()).andReturn("GET").anyTimes();
        expect(requestRepository.isReady()).andReturn(true);
        expect(requestRepository.getRequest("/api/service", "GET")).andReturn(null);
        replay(request, requestRepository);

        mockHandler.handle(request, response);

        assertEquals(MockHandler.SC_UNEXPECTED_CALL, MockedMockHandler.code);

        verify(request, requestRepository);
    }

    @Test
    public void shouldTestForParameterMismatch() throws IOException {
        IRequest mockRequest = createMock(IRequest.class);
        expect(mockRequest.getHeaders()).andReturn(new HashMap<String, String>()).anyTimes();
        expect(mockRequest.getMethod()).andReturn(Method.GET).anyTimes();
        HashMap<String, String[]> params1 = new HashMap<String, String[]>();
        params1.put("param1", new String[] {"value1"});
        expect(mockRequest.getParams()).andReturn(params1).anyTimes();
        expect(requestRepository.isReady()).andReturn(true).anyTimes();
        expect(requestRepository.getRequest("/api/service1", "GET")).andReturn(mockRequest).anyTimes();

        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeaderNames()).andReturn(new MockEnumeration(null)).anyTimes();
        expect(request.getParameterMap()).andReturn(null).anyTimes();
        replay(request, response, mockRequest, requestRepository);

        mockHandler.handle(request, response);
        assertEquals(MockHandler.SC_REQUEST_MISMATCH, MockedMockHandler.code);

        reset(request, response);
        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeaderNames()).andReturn(new MockEnumeration(null)).anyTimes();
        HashMap<String, String[]> params = new HashMap<String, String[]>();
        params.put("param1", new String[]{"something"});
        expect(request.getParameterMap()).andReturn(params).anyTimes();
        replay(request, response);

        mockHandler.handle(request, response);
        assertEquals(MockHandler.SC_REQUEST_MISMATCH, MockedMockHandler.code);

        verify(request, response, requestRepository, mockRequest);
    }

    @Test
    public void shouldTestForHeaderMismatch() throws IOException {
        new Server().expect("/api/service1", Method.GET).withHeader("Accept", "application/json");

        IRequest mockRequest = createMock(IRequest.class);
        expect(requestRepository.getRequest("/api/service1", "GET")).andReturn(mockRequest).anyTimes();
        expect(requestRepository.isReady()).andReturn(true).anyTimes();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        expect(mockRequest.getHeaders()).andReturn(headers).anyTimes();
        expect(mockRequest.getMethod()).andReturn(Method.GET).anyTimes();
        expect(mockRequest.getParams()).andReturn(new HashMap<String, String[]>()).anyTimes();

        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeaderNames()).andReturn(new MockEnumeration(null)).anyTimes();
        expect(request.getParameterMap()).andReturn(null).anyTimes();
        replay(request, requestRepository, response, mockRequest);

        mockHandler.handle(request, response);
        assertEquals(MockHandler.SC_REQUEST_MISMATCH, MockedMockHandler.code);

        new Server().expect("/api/service1", Method.GET).withHeader("Accept", "application/json");

        reset(request, response);
        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeaderNames()).andReturn(new MockEnumeration(new String[]{"Accept"})).anyTimes();
        expect(request.getHeader("Accept")).andReturn("application/json");
        expect(request.getParameterMap()).andReturn(null).anyTimes();
        replay(request, response);

        mockHandler.handle(request, response);
        assertEquals(MockHandler.SC_REQUEST_MISMATCH, MockedMockHandler.code);

        verify(request, requestRepository, response, mockRequest);
    }

    public static class MockedMockHandler extends MockHandler {

        private static int code = 0;

        @Override
        protected void sendResponse(HttpServletRequest request, HttpServletResponse response, String error, int code) throws IOException {
            MockedMockHandler.code = code;
        }
    }

    public class MockEnumeration<E> implements Enumeration<E> {

        private E[] arr;

        private int count = 0;


        public MockEnumeration(E[] arr) {
            this.arr = arr;
        }

        @Override
        public boolean hasMoreElements() {
            return count < (arr == null ? 0 : arr.length);
        }

        @Override
        public E nextElement() {
            return arr[count++];
        }

    }

}
