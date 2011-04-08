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
        return defineServer(uri, false);
    }

    public static IExpectationSetters defineServer(String uri, boolean startServer) {
        ServerControl control = new ServerControl();
        Controls.addControl(control);

        if (startServer) {
            try {
                ServerFactory.startServer(8989);
            } catch (Exception e) {
                throw new RuntimeException("Error while starting the server.");
            }
        }

        return control.defineServer(uri);
    }

    public static IExpectationSetters configure(String json) throws IOException {
        ServerControl control = new ServerControl();
        Controls.addControl(control);
        return control.configure(json);
    }

    public static void main(String args[]) {
        RestMock.defineServer("http://localhost:8989", true);
    }

}
