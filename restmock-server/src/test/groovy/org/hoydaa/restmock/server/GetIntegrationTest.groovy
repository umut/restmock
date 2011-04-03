package org.hoydaa.restmock.server

import org.junit.Test
import org.hoydaa.restmock.client.Server
import org.hoydaa.restmock.client.Method
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentType
import static junit.framework.TestCase.*

/**
 * @author Umut Utkan
 */
class GetIntegrationTest extends SingleRestMockTest {

    @Override
    Server getSetupData() {
        new Server().expect("/service1", Method.GET).andReply("application/json", 200, "{\"param1\" : \"value1\"}")
    }

    @Test
    def void shouldRecordPostRequests() {
        new HTTPBuilder("http://localhost:8989/service1").request(groovyx.net.http.Method.GET, ContentType.JSON) { req ->
            headers.'Accept' = 'application/json'
            response.'200' = { resp, json ->
                assertEquals(json['param1'], "value1")
                logger.info("Expectations are set, ready to roll.")
            }
            response.failure = { resp ->
                fail("Fail")
            }
        }
    }

}
