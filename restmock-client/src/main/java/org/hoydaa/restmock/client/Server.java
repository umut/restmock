package org.hoydaa.restmock.client;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the server that will respond to HTTP calls and contains utility methods to set up the server
 * for mock request that will be made.
 *
 * @author Umut Utkan
 */
public class Server {

    private final static String DEFAULT_URL = "http://localhost:9999";

    private final static String MANAGEMENT_SERVICE = "manage";

    private String url;

    private List<Request> requests = new ArrayList<Request>();

    public Server() {
        this(DEFAULT_URL);
    }

    public Server(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            assert false;
        }

        this.url = url;
    }

    public Server expect(String path, Method method) {
        requests.add(new Request());
        requests.get(requests.size() - 1).setMethod(method);
        requests.get(requests.size() - 1).setPath(path);
        return this;
    }

    public Server withParam(String key, String[] value) {
        requests.get(requests.size() - 1).getParams().put(key, value);
        return this;
    }

    public Server withHeader(String key, String value) {
        requests.get(requests.size() - 1).getHeaders().put(key, value);
        return this;
    }

    public Server andReply(String type, int code, Object rtn) throws IOException {
        requests.get(requests.size() - 1).getResponse().setStream(rtn instanceof InputStream ? (InputStream) rtn : IOUtils.toInputStream(rtn.toString(), "UTF-8"));
        requests.get(requests.size() - 1).getResponse().setType(type);
        requests.get(requests.size() - 1).getResponse().setStatus(code);
        return this;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public Server configure(String json) throws IOException {
        ObjectMapper objectMapper = getObjectMapper();

        return objectMapper.readValue(new StringReader(json), Server.class);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toJsonString() throws IOException {
        StringWriter writer = new StringWriter();

        ObjectMapper objectMapper = getObjectMapper();

        objectMapper.writeValue(writer, this);

        return writer.toString();
    }

    public Request getRequest(String path, Method method) {
        for (Request request : requests) {
            if (request.getPath().equals(path) && request.getMethod().equals(method)) {
                return request;
            }
        }

        return null;
    }

    public void replay() {

    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule myModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
        myModule.addSerializer(new StreamSerializer());
        myModule.addDeserializer(InputStream.class, new StreamDeserializer());
        objectMapper.registerModule(myModule);

        return objectMapper;
    }

}
