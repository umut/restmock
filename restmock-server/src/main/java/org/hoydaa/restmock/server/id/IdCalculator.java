package org.hoydaa.restmock.server.id;

import javax.servlet.http.HttpServletRequest;

/**
 * Calculates the id that will be used to retrieve the stream from the repository based on given request.
 *
 * @author Umut Utkan
 */
public interface IdCalculator {

    String calculate(HttpServletRequest request);

}
