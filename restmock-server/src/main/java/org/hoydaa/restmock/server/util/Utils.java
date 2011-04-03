package org.hoydaa.restmock.server.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Method;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class Utils {

    public static boolean equals(IRequest from, HttpServletRequest o) {
        if (o == null) return false;
        if (from == null) return false;
        if (from == o) return true;

        for (Map.Entry<String, String> entry : from.getHeaders().entrySet()) {
            if (!entry.getValue().equals(o.getHeader(entry.getKey()))) {
                return false;
            }
        }
        if (from.getMethod() != Method.valueOf(o.getMethod().toUpperCase())) return false;
        if (from.getParams() != null ? !from.getParams().equals(o.getParameterMap()) : o.getParameterMap() != null)
            return false;
        if (from.getPath() != null ? !from.getPath().equals(o.getPathInfo()) : o.getPathInfo() != null) return false;

        return true;
    }

}
