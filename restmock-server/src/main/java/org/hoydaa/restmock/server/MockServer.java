package org.hoydaa.restmock.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Starts mock server.
 *
 * @author Umut Utkan
 */
public class MockServer {

    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("/org/hoydaa/restmock/server/spring/beans-mock.xml", MockServer.class);
    }

}