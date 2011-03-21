package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.server.handler.AbstractRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Umut Utkan
 */
public class RestMockHandler extends AbstractRequestHandler {

    private MockHandler mockHandler;

    private ManagerHandler managerHandler;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo().endsWith("manage")) {
            managerHandler.handle(request, response);
        } else {
            mockHandler.handle(request, response);
        }
    }

    public void setMockHandler(MockHandler mockHandler) {
        this.mockHandler = mockHandler;
    }

    public void setManagerHandler(ManagerHandler managerHandler) {
        this.managerHandler = managerHandler;
    }

}
