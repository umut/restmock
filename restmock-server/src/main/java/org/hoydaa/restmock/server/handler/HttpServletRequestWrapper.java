package org.hoydaa.restmock.server.handler;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Method;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class HttpServletRequestWrapper implements IRequest {

    private HttpServletRequest request;


    public HttpServletRequestWrapper(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getPath() {
        return request.getPathInfo();
    }

    @Override
    public Method getMethod() {
        return Method.valueOf(request.getMethod().toUpperCase());
    }

    @Override
    public Map<String, String[]> getParams() {
        return request.getParameterMap();
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
            String key = e.nextElement();
            headers.put(key, request.getHeader(key));
        }

        return headers;
    }

    @Override
    public boolean equals(Object o) {
        throw new RuntimeException("Not supported!");
    }

}
