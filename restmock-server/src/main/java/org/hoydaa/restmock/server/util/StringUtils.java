package org.hoydaa.restmock.server.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.hoydaa.restmock.client.IRequest;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Umut Utkan
 */
public class StringUtils {

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

}
