package org.hoydaa.restmock.server.id;

import javax.servlet.http.HttpServletRequest;

/**
 * Simply returns path as key.
 *
 * @author Umut Utkan
 */
public class DefaultIdCalculator implements IdCalculator {

    @Override
    public String calculate(HttpServletRequest request) {
        return request.getPathInfo();
    }

}
