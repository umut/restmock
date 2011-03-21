package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Server;

/**
 * @author Umut Utkan
 */
public interface RequestRepository {

    public boolean isReady();

    public IRequest getRequest(String path, String method);

    public void setServer(Server server);

}
