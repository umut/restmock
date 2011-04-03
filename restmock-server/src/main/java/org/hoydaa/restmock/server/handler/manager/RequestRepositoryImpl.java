package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.client.IRequest;
import org.hoydaa.restmock.client.Method;
import org.hoydaa.restmock.client.Server;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class RequestRepositoryImpl implements RequestRepository {

    private Server server;

    private Map<IRequest, Integer> callCounts = new HashMap<IRequest, Integer>();


    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public boolean isReady() {
        return null != server;
    }

    @Override
    public IRequest getRequest(HttpServletRequest request) throws RequestRepositoryException {
        IRequest mocked = server.getRequest(request.getPathInfo(), Method.valueOf(request.getMethod().toUpperCase()));
        if(null == mocked) {
            return null;
        }

        int count = callCounts.get(mocked) == null ? 0 : callCounts.get(mocked);
        callCounts.put(mocked, ++count);

        if (mocked.getTimes() != -1 && count > mocked.getTimes()) {
            throw new RequestRepositoryException(RequestRepositoryException.ERROR_EXCEEDED_CALL, "Call time exceeded.");
        }

        return mocked;
    }

}
