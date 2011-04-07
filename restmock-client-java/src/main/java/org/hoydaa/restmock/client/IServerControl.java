package org.hoydaa.restmock.client;

import java.io.IOException;
import java.net.URI;

/**
 * @author Umut Utkan
 */
public interface IServerControl {

    IExpectationSetters defineServer(String uri);

    IExpectationSetters configure(String content) throws IOException;

}
