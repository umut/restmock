package org.hoydaa.restmock.server.repository;

import java.io.InputStream;

/**
 * Simple interface for storing resources
 *
 * @author Umut Utkan
 */
public interface Repository {

    InputStream retrieve(String id);

}
