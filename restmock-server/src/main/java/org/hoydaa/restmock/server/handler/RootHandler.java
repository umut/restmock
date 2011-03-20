package org.hoydaa.restmock.server.handler;

import org.mortbay.jetty.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hello page for the path root path '/'.
 *
 * @author Umut Utkan
 */
public class RootHandler implements RequestHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Mock Server</h1>");
        response.getWriter().println("<span>This is just a mock server that serves for manual/automated tests!</span>");
        ((Request) request).setHandled(true);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
