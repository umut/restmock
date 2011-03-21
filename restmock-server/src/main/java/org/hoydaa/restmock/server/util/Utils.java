package org.hoydaa.restmock.server.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.hoydaa.restmock.client.IRequest;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Umut Utkan
 */
public class Utils {

    public static String toJsonString(IRequest request) {
        StringWriter writer = new StringWriter();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(writer, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    public static boolean equals(IRequest from, IRequest o) {
        if (o == null) return false;
        if (from == null) return false;
        if (from == o) return true;

        if (from.getHeaders() != null ? !from.getHeaders().equals(o.getHeaders()) : o.getHeaders() != null)
            return false;
        if (from.getMethod() != o.getMethod()) return false;
        if (from.getParams() != null ? !from.getParams().equals(o.getParams()) : o.getParams() != null) return false;
        if (from.getPath() != null ? !from.getPath().equals(o.getPath()) : o.getPath() != null) return false;

        return true;
    }

}
