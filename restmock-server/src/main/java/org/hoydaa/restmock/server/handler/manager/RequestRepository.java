package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Server;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Umut Utkan
 */
public interface RequestRepository {

    public boolean isReady();

    public IRequest getRequest(HttpServletRequest req) throws RequestRepositoryException;

    public void setServer(Server server);

}
