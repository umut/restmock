package org.hoydaa.restmock.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the values for the request that is being set up for the mock server.
 *
 * @author Umut Utkan
 */
public class Request implements IRequest {

    private Method method;

    private String path;

    private int times = 1;

    private Map<String, String[]> params = new HashMap<String, String[]>();

    private Map<String, String> headers = new HashMap<String, String>();

    private Response response = new Response();


    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
