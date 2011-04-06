package org.hoydaa.restmock.server.util;

import org.apache.commons.lang.ArrayUtils;
import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Method;
import org.hoydaa.restmock.server.handler.manager.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class Utils {

    public static ResponseStatus equals(IRequest from, HttpServletRequest o) {
        if (o == null) return ResponseStatus.REQUEST_MISMATCH;
        if (from == null) return ResponseStatus.REQUEST_MISMATCH;
        if (from == o) return null;

        for (Map.Entry<String, String> entry : from.getHeaders().entrySet()) {
            if (!entry.getValue().equals(o.getHeader(entry.getKey()))) {
                return ResponseStatus.HEADER_MISMATCH;
            }
        }

        if (from.getMethod() != Method.valueOf(o.getMethod().toUpperCase())) return ResponseStatus.REQUEST_MISMATCH;

        for (Map.Entry<String, String[]> entry : from.getParams().entrySet()) {
            if (!ArrayUtils.isEquals(entry.getValue(), o.getParameterValues(entry.getKey()))) {
                return ResponseStatus.PARAM_MISMATCH;
            }
        }

        return null;
    }

}
