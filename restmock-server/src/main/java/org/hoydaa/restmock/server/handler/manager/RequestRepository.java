package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.model.Request;
import org.hoydaa.restmock.client.model.Server;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Umut Utkan
 */
public interface RequestRepository {

    public boolean isReady();

    public Request getRequest(HttpServletRequest req) throws RequestRepositoryException;

    public void setServer(Server server);

}
