package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.server.handler.AbstractRequestHandler;
import org.hoydaa.restmock.server.handler.HttpServletRequestWrapper;
import org.hoydaa.restmock.server.util.Utils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Umut Utkan
 */
public class MockHandler extends AbstractRequestHandler {

    public static final int SC_EXPECTATIONS_NOT_SET = 901;

    public static final int SC_UNEXPECTED_CALL = 902;

    public static final int SC_REQUEST_MISMATCH = 903;

    private RequestRepository requestRepository;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Assert.notNull(requestRepository, "RequestRepository cannot be null!");

        if (!requestRepository.isReady()) {
            sendResponse(request, response, "Expectations not set, please set the expectations first!", SC_EXPECTATIONS_NOT_SET);
            return;
        }

        IRequest mockRequest = requestRepository.getRequest(request.getPathInfo(), request.getMethod());
        if (null == mockRequest) {
            sendResponse(request, response, "Unexpected call " + request.getMethod() + " - " + request.getPathInfo(), SC_UNEXPECTED_CALL);
            return;
        }

        IRequest wrapped = new HttpServletRequestWrapper(request);
        if (!Utils.equals(mockRequest, wrapped)) {
            sendResponse(request, response, "Recorded request does not match to the request just made!"
                    + "expected: " + mockRequest + ", actual: " + wrapped,
                    SC_REQUEST_MISMATCH);
            return;
        }

        sendResponse(request, response, "Replaying...", HttpServletResponse.SC_OK);
    }

    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

}
