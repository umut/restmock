package org.hoydaa.restmock.client;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Umut Utkan
 */
public class StreamDeserializer extends JsonDeserializer<InputStream> {

    @Override
    public InputStream deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return new ByteArrayInputStream(jsonParser.readValueAs(byte[].class));
    }

}
