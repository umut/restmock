package org.hoydaa.restmock.server.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request handler is handling the request directly via bypassing repositories and post processors. If your mock
 * really requires custom functionality that cannot be handled by using repositories and post processors you can
 * implement your custom request handler.
 *
 * @author Umut Utkan
 */
public interface RequestHandler {

    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
