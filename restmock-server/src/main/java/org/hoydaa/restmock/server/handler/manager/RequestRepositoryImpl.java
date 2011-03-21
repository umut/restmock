package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Method;
import org.hoydaa.restmock.client.Server;

/**
 * @author Umut Utkan
 */
public class RequestRepositoryImpl implements RequestRepository {

    private Server server;


    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public boolean isReady() {
        return null != server;
    }

    @Override
    public IRequest getRequest(String path, String method) {
        IRequest request = server.getRequest(path, Method.valueOf(method));

        return request;
    }

}
