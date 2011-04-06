package org.hoydaa.restmock.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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

    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    private final static String DEFAULT_URL = "http://localhost:9999";

    private final static String MANAGEMENT_SERVICE = "manage";


    private String url;

    private boolean ordered;

    private List<Request> requests = new ArrayList<Request>();


    public Server() {
        this(DEFAULT_URL, false);
    }

    public Server(boolean ordered) {
        this(DEFAULT_URL, ordered);
    }

    public Server(String url) {
        this(url, false);
    }

    public Server(String url, boolean ordered) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            assert false;
        }

        this.url = url;
        this.ordered = ordered;
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

    public Server andReply(String type, int code, String rtn) throws IOException {
        if (rtn.startsWith("local:")) {
            return andReply(type, code, FileUtils.openInputStream(new File(rtn.substring(6))));
        } else if (rtn.startsWith("remote:")) {
            requests.get(requests.size() - 1).getResponse().setContentPath(rtn);
            return andReply(type, code, (InputStream) null);
        } else if (rtn.startsWith("classpath:")) {
            return andReply(type, code, getClass().getResourceAsStream(rtn.substring(10)));
        } else if (rtn.startsWith("http")) {
            requests.get(requests.size() - 1).getResponse().setContentPath(rtn);
            return andReply(type, code, (InputStream) null);
        } else {
            return andReply(type, code, IOUtils.toInputStream(rtn, "UTF-8"));
        }
    }

    public Server andReply(String type, int code, InputStream rtn) throws IOException {
        requests.get(requests.size() - 1).getResponse().setStream(rtn);
        requests.get(requests.size() - 1).getResponse().setType(type);
        requests.get(requests.size() - 1).getResponse().setStatus(code);
        return this;
    }

    public Server times(int times) {
        requests.get(requests.size() - 1).setTimes(times);
        return this;
    }

    public Server anyTimes() {
        return times(-1);
    }

    public void replay() {
        try {
            String json = toJsonString();

            logger.info("About to post expectations\n{}", json);

            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(getUrl() + "/" + MANAGEMENT_SERVICE);
            postMethod.setRequestBody(json);
            int code = httpClient.executeMethod(postMethod);

            if (code != 200) {
                if (code == 400) {
                    logger.error("Error while setting expectations due to problematic input.");
                }
                throw new RuntimeException("Error while setting expectations, return code is " + code);
            }

            logger.info("Expectations are successfully set.");
        } catch (IOException e) {

        }
    }

    public Server reset() {
        try {
            logger.info("About to reset expectations\n{}");

            HttpClient httpClient = new HttpClient();
            DeleteMethod deleteMethod = new DeleteMethod(getUrl() + "/" + MANAGEMENT_SERVICE);
            int code = httpClient.executeMethod(deleteMethod);

            if (code != 200) {
                throw new RuntimeException("Error while resetting expectations, return code is " + code);
            }

            logger.info("Expectations are successfully reset.");
        } catch (IOException e) {

        }

        return this;
    }

    public Server configure(String json) throws IOException {
        ObjectMapper objectMapper = getObjectMapper();

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

        ObjectMapper objectMapper = getObjectMapper();

        objectMapper.writeValue(writer, this);

        return writer.toString();
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
