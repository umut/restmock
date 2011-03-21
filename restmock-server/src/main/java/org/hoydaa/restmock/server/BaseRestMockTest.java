package org.hoydaa.restmock.server;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Umut Utkan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/hoydaa/restmock/server/spring/beans-mock.xml"})
public class BaseRestMockTest {

}
