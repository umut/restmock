package org.hoydaa.restmock.server.processor;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Processes the stream retrieved from repository before writing to response.
 *
 * @author Umut Utkan
 */
public interface PostProcessor {

    InputStream process(InputStream stream, HttpServletRequest request);

}
