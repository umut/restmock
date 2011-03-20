package org.hoydaa.restmock.client;

import java.util.Map;

/**
 * @author Umut Utkan
 */
public interface IRequest {

    String getPath();

    Method getMethod();

    Map<String, String[]> getParams();

    Map<String, String> getHeaders();

}
