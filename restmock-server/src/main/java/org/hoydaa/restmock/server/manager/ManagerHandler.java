package org.hoydaa.restmock.server.manager;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.model.Server;
import org.hoydaa.restmock.server.handler.AbstractRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Umut Utkan
 */
public class ManagerHandler extends AbstractRequestHandler {

    private final static Logger logger = LoggerFactory.getLogger(ManagerHandler.class);


    private RequestRepository requestRepository;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Assert.notNull(requestRepository, "RequestRepository cannot be null!");

        if (request.getMethod().equals("DELETE")) {
            requestRepository.setServer(null);

            logger.info("Resetting expectations.");

            sendResponse(request, response, "Successfully reset expectations", HttpServletResponse.SC_OK);
        } else {
            try {
                String json = IOUtils.toString(request.getInputStream(), "UTF-8");

                logger.info("Setting expectations to\n{}", json);

                requestRepository.setServer(new Server().configure(json));
                sendResponse(request, response, "Successfully recorded expectations.", HttpServletResponse.SC_OK);
            } catch (Exception e) {
                sendError(request, response, "Error while recording expectations: " + e.getMessage());
            }
        }
    }

    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

}
