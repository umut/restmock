package org.hoydaa.restmock.server.handler;

import org.mortbay.jetty.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Umut Utkan
 */
abstract public class AbstractRequestHandler implements RequestHandler {

    protected void sendError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        sendResponse(request, response, message, HttpServletResponse.SC_BAD_REQUEST);
    }

    protected void sendExpectationError(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        sendResponse(request, response, message, 999);
    }

    protected void sendResponse(HttpServletRequest request, HttpServletResponse response, String error, int code) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Mock Server</h1>");
        response.getWriter().println("<span>" + error + "</span>");
        ((Request) request).setHandled(true);
        response.setStatus(code);
    }

}
