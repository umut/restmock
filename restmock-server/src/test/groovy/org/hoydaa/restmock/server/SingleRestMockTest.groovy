package org.hoydaa.restmock.server

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.junit.Before
import org.slf4j.LoggerFactory
import org.hoydaa.restmock.client.model.Server

/**
 * @author Umut Utkan
 */
abstract class SingleRestMockTest extends BaseRestMockTest {

    def static logger = LoggerFactory.getLogger(SingleRestMockTest.class)

    @Before
    def void before() {
        logger.info("Setting up the mock server with \n{}", setupData)
        setupServer setupData.toJsonString()
    }

    abstract def Server getSetupData()

    def setupServer(data) {
        new HTTPBuilder("http://localhost:8989/manage").request(Method.POST, ContentType.JSON) { req ->
            requestContentType = ContentType.TEXT
            body = data
            response.'200' = { resp ->
                logger.info("Expectations are set, ready to roll.")
            }
            response.'400' = {
                throw new RuntimeException("Error while setting expectations due to problematic input.");
            }
            response.failure = { resp ->
                throw new RuntimeException("Error while setting expectations, error code: " + resp.statusLine);
            }
        }
    }

}
