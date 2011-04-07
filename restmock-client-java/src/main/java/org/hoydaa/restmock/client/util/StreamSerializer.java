package org.hoydaa.restmock.client.util;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Umut Utkan
 */
public class StreamSerializer extends JsonSerializer<InputStream> {

    @Override
    public void serialize(InputStream inputStream, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeBinary(IOUtils.toByteArray(inputStream));
    }

    @Override
    public Class<InputStream> handledType() {
        return InputStream.class;
    }

}
