package org.hoydaa.restmock.server.handler.manager;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.model.Method;
import org.hoydaa.restmock.model.Request;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import static junit.framework.TestCase.assertEquals;
import static org.easymock.classextension.EasyMock.*;

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
        expect(requestRepository.getRequest(isA(HttpServletRequest.class))).andThrow(new RequestRepositoryException(ResponseStatus.NOT_READY));
        replay(request, requestRepository);

        mockHandler.handle(request, response);

        assertEquals(ResponseStatus.NOT_READY.getCode(), MockedMockHandler.code);

        verify(request, requestRepository);
    }

    @Test
    public void shouldTestForExistence() throws IOException {
        expect(request.getPathInfo()).andReturn("/api/service").anyTimes();
        expect(request.getMethod()).andReturn("GET").anyTimes();
        expect(requestRepository.getRequest(isA(HttpServletRequest.class))).andReturn(null);
        replay(request, requestRepository);

        mockHandler.handle(request, response);

        assertEquals(ResponseStatus.UNEXPECTED_CALL.getCode(), MockedMockHandler.code);

        verify(request, requestRepository);
    }

    @Test
    public void shouldTestForParameterMismatch() throws IOException {
        Request mockRequest = new Request();
        mockRequest.setMethod(Method.GET);
        mockRequest.getParams().put("param1", new String[]{"value1"});

        expect(requestRepository.isReady()).andReturn(true).anyTimes();
        expect(requestRepository.getRequest(isA(HttpServletRequest.class))).andReturn(mockRequest).anyTimes();

        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeaderNames()).andReturn(new MockEnumeration(null)).anyTimes();
        expect(request.getParameterValues("param1")).andReturn(null).anyTimes();
        replay(request, response, requestRepository);

        mockHandler.handle(request, response);
        assertEquals(ResponseStatus.PARAM_MISMATCH.getCode(), MockedMockHandler.code);

        reset(request, response);
        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeaderNames()).andReturn(new MockEnumeration(null)).anyTimes();
        expect(request.getParameterValues("param1")).andReturn(new String[]{"something"}).anyTimes();
        replay(request, response);

        mockHandler.handle(request, response);
        assertEquals(ResponseStatus.PARAM_MISMATCH.getCode(), MockedMockHandler.code);

        verify(request, response, requestRepository);
    }

    //TODO: fix this later
    @Ignore
    @Test
    public void shouldTestForHeaderMismatch() throws IOException {
        //new Server().expect("/api/service1", Method.GET).withHeader("Accept", "application/json");

        Request mockRequest = new Request();
        mockRequest.setPath("/api/service1");
        mockRequest.setMethod(Method.GET);
        mockRequest.getResponse().setStatus(200);
        mockRequest.getResponse().setType("application/json");
        mockRequest.getResponse().setStream(IOUtils.toInputStream("{}", "UTF-8"));

        expect(requestRepository.getRequest(isA(HttpServletRequest.class))).andReturn(mockRequest).anyTimes();
        expect(requestRepository.isReady()).andReturn(true).anyTimes();

        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getHeader("Accept")).andReturn(null).anyTimes();
        expect(request.getParameterMap()).andReturn(null).anyTimes();
        expect(response.getWriter()).andReturn(new PrintWriter(new ByteArrayOutputStream()));
        response.setStatus(200);
        response.setContentType("application/json");
        expectLastCall();
        replay(request, requestRepository, response);

        mockHandler.handle(request, response);

        //new Server().expect("/api/service1", Method.GET).withHeader("Accept", "application/json");

        reset(request, response);
        expect(request.getPathInfo()).andReturn("/api/service1").anyTimes();
        expect(request.getMethod()).andReturn(Method.GET.name()).anyTimes();
        expect(request.getParameterMap()).andReturn(null).anyTimes();
        expect(response.getWriter()).andReturn(new PrintWriter(new ByteArrayOutputStream()));
        response.setStatus(200);
        response.setContentType("application/json");
        expectLastCall();
        replay(request, response);

        mockHandler.handle(request, response);

        verify(request, requestRepository, response);
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
