package org.hoydaa.restmock.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.hoydaa.restmock.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the server that will respond to HTTP calls and contains utility methods to set up the server
 * for mock request that will be made.
 *
 * @author Umut Utkan
 */
public class Server {

    private final static Logger logger = LoggerFactory.getLogger(Server.class);


    private String url;

    private boolean ordered;

    private List<Request> requests = new ArrayList<Request>();


    public Server() {

    }

    public Server(String url) {
        this.url = url;
    }

    public Server configure(String json) throws IOException {
        ObjectMapper objectMapper = JacksonUtils.getObjectMapper();

        return objectMapper.readValue(new StringReader(json), Server.class);
    }

    public List<Request> getRequests() {
        return requests;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public Request getRequest(String path, Method method) {
        for (Request request : requests) {
            if (request.getPath().equals(path) && request.getMethod().equals(method)) {
                return request;
            }
        }

        return null;
    }

    public String toJsonString() throws IOException {
        StringWriter writer = new StringWriter();

        ObjectMapper objectMapper = JacksonUtils.getObjectMapper();

        objectMapper.writeValue(writer, this);

        return writer.toString();
    }

}
