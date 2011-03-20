package org.hoydaa.restmock.server;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.server.handler.RequestHandler;
import org.hoydaa.restmock.server.id.IdCalculator;
import org.hoydaa.restmock.server.processor.PostProcessor;
import org.hoydaa.restmock.server.repository.Repository;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main handler, the entry point for the container.
 *
 * @author Umut utkan
 */
public class MockHandler extends AbstractHandler {

    private final static Logger logger = LoggerFactory.getLogger(MockHandler.class);


    private Map<String, IdCalculator> idCalculators = new LinkedHashMap<String, IdCalculator>();

    private Map<String, PostProcessor> postProcessors = new LinkedHashMap<String, PostProcessor>();

    private Map<String, RequestHandler> requestHandlers = new LinkedHashMap<String, RequestHandler>();

    private Repository repository;


    @Override
    public void handle(String s, HttpServletRequest request, HttpServletResponse response, int dispatch)
            throws IOException, ServletException {
        String pathInfo = request.getPathInfo();

        RequestHandler requestHandler = get(requestHandlers, pathInfo);
        if (null != requestHandler) {
            requestHandler.handle(request, response);
            return;
        }

        IdCalculator idCalculator = get(idCalculators, pathInfo);
        Assert.notNull(idCalculator, "Id calculator cannot be null, fix your configuration.");

        InputStream stream = repository.retrieve(idCalculator.calculate(request));
        if (null != stream) {
            response.setStatus(HttpServletResponse.SC_OK);

            PostProcessor processor = get(postProcessors, pathInfo);
            if (null != processor) {
                stream = processor.process(stream, request);
            }

            IOUtils.copy(stream, response.getWriter(), "UTF-8");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        response.setContentType("application/json");
        ((Request) request).setHandled(true);
    }

    private <T extends Object> T get(Map<String, T> toSearch, String path) {
        for (String key : toSearch.keySet()) {
            if (path.matches(key)) {
                return toSearch.get(key);
            }
        }

        return null;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setIdCalculators(Map<String, IdCalculator> idCalculators) {
        this.idCalculators = idCalculators;
    }

    public void setPostProcessors(Map<String, PostProcessor> postProcessors) {
        this.postProcessors = postProcessors;
    }

    public void setRequestHandlers(Map<String, RequestHandler> requestHandlers) {
        this.requestHandlers = requestHandlers;
    }

}
