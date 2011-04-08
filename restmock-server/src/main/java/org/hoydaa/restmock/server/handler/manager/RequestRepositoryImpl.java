package org.hoydaa.restmock.server.handler.manager;

import org.hoydaa.restmock.model.Method;
import org.hoydaa.restmock.model.Request;
import org.hoydaa.restmock.model.Server;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class RequestRepositoryImpl implements RequestRepository {

    private Server server;

    private Map<Request, Integer> callCounts = new HashMap<Request, Integer>();


    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public boolean isReady() {
        return null != server;
    }

    @Override
    public Request getRequest(HttpServletRequest request) throws RequestRepositoryException {
        if(null == server) {
            throw new RequestRepositoryException(ResponseStatus.NOT_READY);
        }

        Request mocked = server.getRequest(request.getPathInfo(), Method.valueOf(request.getMethod().toUpperCase()));
        if(null == mocked) {
            return null;
        }

        int count = callCounts.get(mocked) == null ? 0 : callCounts.get(mocked);
        callCounts.put(mocked, ++count);

        if (mocked.getTimes() != -1 && count > mocked.getTimes()) {
            throw new RequestRepositoryException(ResponseStatus.EXCEEDED_CALL);
        }

        return mocked;
    }

}
