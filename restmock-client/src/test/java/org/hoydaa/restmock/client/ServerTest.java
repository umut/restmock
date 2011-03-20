package org.hoydaa.restmock.client;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Umut Utkan
 */
public class ServerTest {

    @Test
    public void shouldRecordRequest() throws IOException {
        Server server = new Server("http://localhost:8989")
                .expect("/dummy/service", Method.GET)
                .withParam("param1", new String[]{"value1"})
                .withHeader(Headers.ACCEPT, "text/plain")
                .andReply("application/json", HttpStatus.SC_OK, IOUtils.toInputStream("{}"))
                .expect("/dummy/service2", Method.POST);

        assertServer(server);
    }

    @Test
    public void shouldBeConfiguredFromFile() throws IOException {
        String str = "{\"requests\":[{\"method\":\"GET\",\"path\":\"/dummy/service\",\"headers\":{\"Accept\":\"text/plain\"},\"params\":{\"param1\":[\"value1\"]},\"response\":{\"type\":\"application/json\",\"status\":200,\"stream\":\"e30=\"}},{\"method\":\"POST\",\"path\":\"/dummy/service2\",\"headers\":{},\"params\":{},\"response\":{\"type\":null,\"status\":0,\"stream\":null}}],\"url\":\"http://localhost:8989\"}";

        System.out.println(str);

        Server server = new Server().configure(str);

        assertServer(server);
    }

    private void assertServer(Server server) throws IOException {
        assertEquals(2, server.getRequests().size());

        assertEquals("http://localhost:8989", server.getUrl());
        assertEquals("/dummy/service", server.getRequests().get(0).getPath());
        assertEquals(Method.GET, server.getRequests().get(0).getMethod());
        assertEquals("value1", server.getRequests().get(0).getParams().get("param1")[0]);
        assertEquals("text/plain", server.getRequests().get(0).getHeaders().get("Accept"));
        assertEquals("application/json", server.getRequests().get(0).getResponse().getType());
        assertEquals("{}", IOUtils.toString(server.getRequests().get(0).getResponse().getStream()));
        assertEquals(200, server.getRequests().get(0).getResponse().getStatus());

        assertEquals("/dummy/service2", server.getRequests().get(1).getPath());
        assertEquals(Method.POST, server.getRequests().get(1).getMethod());
    }

}
