package org.hoydaa.restmock.server.id;

import javax.servlet.http.HttpServletRequest;

/**
 * Calculates the id based on request path and query string variables.
 * <p/>
 * If the request URL is http://host/service/users?id=umut.utkan then the id will be
 * service/users_id_umut.utkan
 *
 * @author Umut Utkan
 */
public class QueryStringAwareIdCalculator extends DefaultIdCalculator {

    @Override
    public String calculate(HttpServletRequest request) {
        StringBuilder id = new StringBuilder(super.calculate(request));
        if (request.getQueryString() != null) {
            String[] queryParams = request.getQueryString().split("&");
            for (String queryParam : queryParams) {
                String[] parts = queryParam.split("=");
                id.append("_");
                id.append(parts[0]);
                id.append("_");
                id.append(parts[1]);
            }
        }

        return id.toString();
    }

}
