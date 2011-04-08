package org.hoydaa.restmock.client;

import org.hoydaa.restmock.model.Request;

/**
 * @author Umut Utkan
 */
public interface IServerState {

    void expect(Request request);

}
