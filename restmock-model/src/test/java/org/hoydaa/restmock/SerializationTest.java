package org.hoydaa.restmock;

import org.apache.commons.io.IOUtils;
import org.hoydaa.restmock.model.Method;
import org.hoydaa.restmock.model.Request;
import org.hoydaa.restmock.model.Response;
import org.hoydaa.restmock.model.Server;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Umut Utkan
 */
public class SerializationTest {

    private final static String JSON = "{\"requests\":[{\"method\":\"GET\",\"path\":\"/service1\",\"headers\":{\"Accept\":\"application/json\"},\"params\":{\"page\":[\"1\"]},\"response\":{\"type\":\"application/json\",\"status\":200,\"stream\":\"e30=\",\"contentPath\":null},\"times\":1}],\"url\":\"http://localhost:8989\",\"ordered\":false}";

    @Test
    public void shouldSerializeToJson() throws IOException {
        Server server = new Server("http://localhost:8989");
        Response response1 = new Response();
        response1.setStatus(200);
        response1.setType("application/json");
        response1.setStream(IOUtils.toInputStream("{}"));
        Request request1 = new Request();
        request1.setMethod(Method.GET);
        request1.setPath("/service1");
        request1.setResponse(response1);
        request1.getHeaders().put("Accept", "application/json");
        request1.getParams().put("page", new String[]{"1"});
        server.getRequests().add(request1);

        assertEquals(JSON, server.toJsonString());
    }

    @Test
    public void shouldDeserializeFromJson() throws IOException {
        Server server = new Server().configure(JSON);
        assertEquals("http://localhost:8989", server.getUrl());
        assertEquals("/service1", server.getRequests().get(0).getPath());
        assertEquals(Method.GET, server.getRequests().get(0).getMethod());
        assertEquals("application/json", server.getRequests().get(0).getHeaders().get("Accept"));
        assertEquals("1", server.getRequests().get(0).getParams().get("page")[0]);
        assertEquals(200, server.getRequests().get(0).getResponse().getStatus());
        assertEquals("application/json", server.getRequests().get(0).getResponse().getType());
        assertEquals("{}", IOUtils.toString(server.getRequests().get(0).getResponse().getStream()));
    }

}
