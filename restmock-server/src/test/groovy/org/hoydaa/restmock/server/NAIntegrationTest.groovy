package org.hoydaa.restmock.server

import groovyx.net.http.HTTPBuilder
import org.junit.Test
import groovyx.net.http.ContentType
import static junit.framework.TestCase.*
import org.hoydaa.restmock.server.handler.manager.MockHandler
import org.hoydaa.restmock.client.Server
import org.hoydaa.restmock.client.Method

/**
 * @author Umut Utkan
 */
class NAIntegrationTest extends BaseRestMockTest {

    @Test
    def void shouldReturnNotReadyResponse() {
        new Server("http://localhost:8989").reset();
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.success = { resp ->
                fail("Should not have returned " + resp.statusLine.statusCode)
            }
            response.failure = { resp ->
                assertEquals(MockHandler.SC_EXPECTATIONS_NOT_SET, resp.statusLine.statusCode)
            }
        }
    }

    @Test
    def void shouldTakeTimesIntoAccount() {
        new Server("http://localhost:8989").expect("/service1", Method.GET).andReply("application/json", 200, "{\"param1\" : \"value1\"}").times(3).replay()
        for (int i = 0; i < 3; i++) {
            new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
                headers.'Accept' = 'application/json'
                response.'200' = { resp, json ->
                    assertEquals(json['param1'], 'value1')
                }
                response.failure = { resp ->
                    fail("Should not have failed.")
                }
            }
        }
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.'200' = { resp, json ->
                fail("Should have failed.")
            }
            response.failure = { resp ->
                assertEquals(904, resp.statusLine.statusCode)
            }
        }
    }

    @Test
    def void shouldTakeHeadersIntoAccount() {
        new Server("http://localhost:8989").reset().expect("/service1", Method.GET).withHeader("Dummy", "DummyValue").andReply("application/json", 200, "{\"param1\" : \"value1\"}").times(2).replay();
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.success = { resp ->
                fail("Should not have succeeded.")
            }
            response.failure = { resp ->
                assertEquals(903, resp.statusLine.statusCode)
            }
        }
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            headers.'Dummy' = 'DummyValue'
            response.success = { resp, json ->
                assertEquals(200, resp.statusLine.statusCode)
                assertEquals(json['param1'], 'value1')
            }
            response.failure = { resp ->
                fail("Should not have failed.")
            }
        }
    }

}
