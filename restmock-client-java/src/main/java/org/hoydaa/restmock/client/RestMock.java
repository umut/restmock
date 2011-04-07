package org.hoydaa.restmock.client;

import org.hoydaa.restmock.client.internal.Controls;
import org.hoydaa.restmock.client.internal.ServerControl;

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

    public static IExpectationSetters configure(String json) throws IOException {
        ServerControl control = new ServerControl();
        Controls.addControl(control);
        return control.configure(json);
    }

}
