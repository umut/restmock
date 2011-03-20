package org.hoydaa.restmock.server.processor;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Substitutes query string parameters into the stream if they exist in the form of the following.
 * <p/>
 * You are requesting http://host/service/users?id=umut.utkan and you want the value umut.utkan to be in the stream,
 * then use the notation {{id}} within your content. It will be then replaced by this processor.
 *
 * @author Umut Utkan
 */
public class QueryParamSubstitutor implements PostProcessor {

    private final static Logger logger = LoggerFactory.getLogger(QueryParamSubstitutor.class);


    // I know not very performant (not streaming), in the end it is just a mock framework :)
    @Override
    public InputStream process(InputStream stream, HttpServletRequest request) {
        try {
            String content = IOUtils.toString(stream, "UTF-8");
            Map<String, Object> map = new HashMap<String, Object>();
            map.putAll(request.getParameterMap());
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof String[]) {
                    String[] values = (String[]) value;
                    if (values.length == 1) {
                        map.put(key, values[0]);
                    }
                }

            }
            content = StrSubstitutor.replace(content, map, "{{", "}}");
            return IOUtils.toInputStream(content, "UTF-8");
        } catch (IOException e) {
            logger.error("Error while reading stream.");

            return null;
        }
    }

}
