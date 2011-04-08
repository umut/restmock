package org.hoydaa.restmock.server;

import org.hoydaa.restmock.server.handler.manager.ManagerHandler;
import org.hoydaa.restmock.server.handler.manager.RestMockHandler;
import org.mortbay.jetty.Server;

/**
 * @author Umut Utkan
 */
public class ServerFactory {

    public static void startServer(int port) throws Exception {
        ManagerHandler managerHandler = new ManagerHandler();
        org.hoydaa.restmock.server.handler.manager.MockHandler mockHandler = new org.hoydaa.restmock.server.handler.manager.MockHandler();
        RestMockHandler restMockHandler = new RestMockHandler();
        restMockHandler.setManagerHandler(managerHandler);
        restMockHandler.setMockHandler(mockHandler);

        MockHandler mainHandler = new MockHandler();
        mainHandler.putRequestHandler(".*", restMockHandler);

        Server server = new Server(port);
        server.setHandler(mainHandler);
        server.start();
    }

}
