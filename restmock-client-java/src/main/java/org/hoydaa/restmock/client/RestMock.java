package org.hoydaa.restmock.client;

import org.hoydaa.restmock.client.internal.Controls;
import org.hoydaa.restmock.client.internal.ServerControl;
import org.hoydaa.restmock.server.ServerFactory;

import java.io.IOException;

/**
 * @author Umut Utkan
 */
public class RestMock {

    public static IExpectationSetters defineServer(String uri) {
        ServerControl control = new ServerControl();
        Controls.addControl(control);
        return control.defineServer(uri);
    }

    public static IExpectationSetters startServer(int port) {
        try {
            ServerFactory.createServer(8989);
        } catch (Exception e) {
            throw new RuntimeException("Error while starting the server.");
        }

        return defineServer("http://localhost:" + port);
    }

    public static IExpectationSetters configure(String json) throws IOException {
        ServerControl control = new ServerControl();
        Controls.addControl(control);
        return control.configure(json);
    }

}
