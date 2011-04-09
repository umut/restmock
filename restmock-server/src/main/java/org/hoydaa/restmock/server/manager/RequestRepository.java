package org.hoydaa.restmock.server.manager;


import org.hoydaa.restmock.model.Request;
import org.hoydaa.restmock.model.Server;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Umut Utkan
 */
public interface RequestRepository {

    public boolean isReady();

    public Request getRequest(HttpServletRequest req) throws RequestRepositoryException;

    public void setServer(Server server);

}
