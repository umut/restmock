package org.hoydaa.restmock.client.model;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.client.RestMock;
import org.hoydaa.restmock.client.internal.ServerControl;
import org.hoydaa.restmock.client.model.Headers;
import org.hoydaa.restmock.client.model.Method;
import org.hoydaa.restmock.client.model.Server;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Umut Utkan
 */
public class ServerTest {

    @Test
    public void shouldRecordRequest() throws IOException {
        ServerControl control = (ServerControl) RestMock.defineServer("http://localhost:8989")
                .expect("/dummy/service", Method.GET)
                .withParam("param1", new String[]{"value1"})
                .withHeader(Headers.ACCEPT, "text/plain")
                .andReply("application/json", HttpStatus.SC_OK, "{}")
                .times(10)
                .expect("/dummy/service2", Method.POST)
                .anyTimes();
        control.finish();

        assertServer(control.getServer());
    }

    @Test
    public void shouldBeConfiguredFromFile() throws IOException {
        String json = "{\"requests\":[{\"times\":10,\"method\":\"GET\",\"path\":\"/dummy/service\",\"headers\":{\"Accept\":\"text/plain\"},\"params\":{\"param1\":[\"value1\"]},\"response\":{\"type\":\"application/json\",\"status\":200,\"stream\":\"e30=\"}},{\"times\":-1,\"method\":\"POST\",\"path\":\"/dummy/service2\",\"headers\":{},\"params\":{},\"response\":{\"type\":null,\"status\":0,\"stream\":null}}],\"url\":\"http://localhost:8989\"}";

        ServerControl control = (ServerControl) RestMock.configure(json);

        assertServer(control.getServer());
    }

    private void assertServer(Server server) throws IOException {
        assertEquals(2, server.getRequests().size());
        assertEquals(false, server.isOrdered());

        assertEquals("http://localhost:8989", server.getUrl());
        assertEquals("/dummy/service", server.getRequests().get(0).getPath());
        assertEquals(Method.GET, server.getRequests().get(0).getMethod());
        assertEquals("value1", server.getRequests().get(0).getParams().get("param1")[0]);
        assertEquals("text/plain", server.getRequests().get(0).getHeaders().get("Accept"));
        assertEquals("application/json", server.getRequests().get(0).getResponse().getType());
        assertEquals("{}", IOUtils.toString(server.getRequests().get(0).getResponse().getStream()));
        assertEquals(200, server.getRequests().get(0).getResponse().getStatus());
        assertEquals(10, server.getRequests().get(0).getTimes());

        assertEquals("/dummy/service2", server.getRequests().get(1).getPath());
        assertEquals(Method.POST, server.getRequests().get(1).getMethod());
        assertEquals(-1, server.getRequests().get(1).getTimes());
    }

}
