package org.hoydaa.restmock.server.manager;

import org.hoydaa.restmock.server.handler.AbstractRequestHandler;
import org.springframework.util.Assert;

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
            Assert.notNull(managerHandler, "Manager handler cannot be null.");

            managerHandler.handle(request, response);
        } else {
            Assert.notNull(mockHandler, "Mock handler cannot be null.");

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
