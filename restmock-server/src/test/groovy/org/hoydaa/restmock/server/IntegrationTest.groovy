package org.hoydaa.restmock.server

import groovyx.net.http.HTTPBuilder
import org.junit.Test
import groovyx.net.http.ContentType

import org.hoydaa.restmock.model.Method
import org.hoydaa.restmock.server.handler.manager.ResponseStatus
import static org.junit.Assert.*
import org.hoydaa.restmock.client.RestMock

/**
 * @author Umut Utkan
 */
class IntegrationTest extends BaseRestMockTest {

    @Test
    def void shouldReturnNotReadyResponse() {
        RestMock.defineServer("http://localhost:8989").reset()
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.success = { resp ->
                fail("Should not have returned " + resp.statusLine.statusCode)
            }
            response.failure = { resp ->
                assertEquals(ResponseStatus.NOT_READY.getCode(), resp.statusLine.statusCode)
            }
        }
    }

    @Test
    def void shouldTakeTimesIntoAccount() {
        RestMock.defineServer("http://localhost:8989").expect("/service1", org.hoydaa.restmock.model.Method.GET).andReply("application/json", 200, "{\"param1\" : \"value1\"}").times(3).replay()
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
                assertEquals(ResponseStatus.EXCEEDED_CALL.getCode(), resp.statusLine.statusCode)
            }
        }
    }

    @Test
    def void shouldTakeHeadersIntoAccount() {
        RestMock.defineServer("http://localhost:8989").reset().expect("/service1", org.hoydaa.restmock.model.Method.GET).withHeader("Dummy", "DummyValue").andReply("application/json", 200, "{\"param1\" : \"value1\"}").times(2).replay();
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.success = { resp ->
                fail("Should not have succeeded.")
            }
            response.failure = { resp ->
                assertEquals(ResponseStatus.HEADER_MISMATCH.getCode(), resp.statusLine.statusCode)
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

    @Test
    def void shouldTakeRequestParametersIntoAccount() {
        RestMock.defineServer("http://localhost:8989").reset().expect("/service1", org.hoydaa.restmock.model.Method.GET).withParam("param1", ["value1"] as String[]).andReply("application/json", 200, "{\"param1\" : \"value1\"}").times(2).replay();
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.success = { resp ->
                fail("Should not have succeeded.")
            }
            response.failure = { resp ->
                assertEquals(ResponseStatus.PARAM_MISMATCH.getCode(), resp.statusLine.statusCode)
            }
        }
        new HTTPBuilder("http://localhost:8989/service1?param1=value1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.success = { resp, json ->
                assertEquals(200, resp.statusLine.statusCode)
                assertEquals(json['param1'], 'value1')
            }
            response.failure = { resp ->
                fail("Should not have failed with " + resp.statusLine.statusCode)
            }
        }
    }

}
