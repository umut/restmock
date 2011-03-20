package org.hoydaa.restmock.server.handler;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Request;
import org.hoydaa.restmock.client.Server;
import org.hoydaa.restmock.server.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class Manager extends AbstractRequestHandler {

    public static final int SC_EXPECTATIONS_NOT_SET = 901;

    public static final int SC_NOT_EXPECTED_PATH = 902;

    public static final int SC_WRONG_METHOD = 903;

    public static final int SC_REQUEST_MISMATCH = 904;

    protected static Server server = null;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo().endsWith("manage")) {
            try {
                server = new Server().configure(IOUtils.toString(request.getInputStream(), "UTF-8"));
                sendResponse(request, response, "Successfully recorded expectations.", HttpServletResponse.SC_OK);
            } catch (Exception e) {
                sendError(request, response, "Error while recording expectations: " + e.getMessage());
            }
        } else {
            handleExpectation(request, response, server);
        }
    }

    private void handleExpectation(HttpServletRequest request, HttpServletResponse response, Server server) throws IOException {
        if (null == server) {
            sendResponse(request, response, "Expectations not set, please set the expectations first!", SC_EXPECTATIONS_NOT_SET);
            return;
        }

        Request mockRequest = server.getRequests().remove(0);
        if (!request.getPathInfo().equals(mockRequest.getPath())) {
            sendResponse(request, response, "Requested path '" + request.getPathInfo() +
                    "' does not match the expected path '" + mockRequest.getPath() + "'.", SC_NOT_EXPECTED_PATH);
            return;
        }

        if (!request.getMethod().equals(mockRequest.getMethod().name())) {
            sendResponse(request, response, "HTTP method mismatch, expected: " + mockRequest.getMethod().name() +
                    ", actual: " + request.getMethod(), SC_WRONG_METHOD);
            return;
        }

        IRequest wrapped = new HttpServletRequestWrapper(request);
        if (!mockRequest.equals(wrapped)) {
            sendResponse(request, response, "Recorded request does not match to the request just made!"
                    + "expected: " + StringUtils.toJsonString(mockRequest) + ", actual: " + StringUtils.toJsonString(wrapped),
                    SC_REQUEST_MISMATCH);
            return;
        }

        sendResponse(request, response, "Replaying...", HttpServletResponse.SC_OK);
    }

}
