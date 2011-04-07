package org.hoydaa.restmock.client;

import org.hoydaa.restmock.client.model.Request;
import org.hoydaa.restmock.client.model.Response;

/**
 * @author Umut Utkan
 */
public interface IServerState {

    void expect(Request request);

}
