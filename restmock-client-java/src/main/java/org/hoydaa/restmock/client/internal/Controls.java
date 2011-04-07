package org.hoydaa.restmock.client.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Umut Utkan
 */
public class Controls {

    private final static ThreadLocal<List<ServerControl>> servers = new ThreadLocal<List<ServerControl>>();

    static {
        servers.set(new ArrayList<ServerControl>());
    }

    public static void addControl(ServerControl control) {
        servers.get().add(control);
    }

}
