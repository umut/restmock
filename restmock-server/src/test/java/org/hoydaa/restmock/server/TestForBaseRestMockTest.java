package org.hoydaa.restmock.server;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.client.Method;
import org.hoydaa.restmock.client.Server;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Umut Utkan
 */
public class TestForBaseRestMockTest extends BaseRestMockTest {

    @Test
    public void shouldDoThis() throws IOException {
        new Server().expect("/api/service1", Method.GET).andReply("application/json", 200, IOUtils.toInputStream("{}")).replay();
    }

}
