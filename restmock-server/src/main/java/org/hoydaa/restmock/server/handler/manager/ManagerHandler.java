package org.hoydaa.restmock.server.handler.manager;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.client.Server;
import org.hoydaa.restmock.server.handler.AbstractRequestHandler;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Umut Utkan
 */
public class ManagerHandler extends AbstractRequestHandler {

    private RequestRepository requestRepository;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Assert.notNull(requestRepository, "RequestRepository cannot be null!");

        try {
            requestRepository.setServer(new Server().configure(IOUtils.toString(request.getInputStream(), "UTF-8")));
            sendResponse(request, response, "Successfully recorded expectations.", HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendError(request, response, "Error while recording expectations: " + e.getMessage());
        }
    }

    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

}
