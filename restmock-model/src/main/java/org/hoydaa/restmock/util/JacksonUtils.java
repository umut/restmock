package org.hoydaa.restmock.util;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.InputStream;

/**
 * @author Umut Utkan
 */
public class JacksonUtils {

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();
            SimpleModule myModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
            myModule.addSerializer(new StreamSerializer());
            myModule.addDeserializer(InputStream.class, new StreamDeserializer());
            objectMapper.registerModule(myModule);
        }

        return objectMapper;
    }

}
