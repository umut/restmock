package org.hoydaa.restmock.client.internal;

import org.hoydaa.restmock.client.IServerState;
import org.hoydaa.restmock.client.model.Request;
import org.hoydaa.restmock.client.model.Response;
import org.hoydaa.restmock.client.model.Server;

/**
 * @author Umut Utkan
 */
public class ServerState implements IServerState {

    private Server server;


    public ServerState(Server server) {
        this.server = server;
    }

    public ServerState(String uri) {
        this.server = new Server(uri);
    }


    @Override
    public void expect(Request request) {
        this.server.getRequests().add(request);
    }

    public Server getServer() {
        return server;
    }

}
