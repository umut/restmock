package org.hoydaa.restmock.server.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Simple resource based repository, since it uses Spring's resource abstraction you can use it with a file system
 * resource, classpath resource, url resource, etc.
 * <p/>
 * The only difference is the repository root, see the examples below.
 * classpath:com/sony/forest/roots/cde/mock/mock-repo
 * file:/home/umut/temp/mock-repo
 * url:http://host/mock-repo
 * etc.
 *
 * @author Umut Utkan
 */
public class ResourceRepository implements Repository {

    private final static Logger logger = LoggerFactory.getLogger(ResourceRepository.class);


    private Resource repositoryRoot;


    @Override
    public InputStream retrieve(String id) {
        Resource resource = null;
        try {
            resource = repositoryRoot.createRelative(id);
        } catch (IOException e) {
            Assert.isTrue(false, "Something is wrong, we should not be here since we know that root resource exists.");
        }

        if (!resource.exists()) {
            logger.debug("Resource {} does not exists for id {}.", resource, id);

            return null;
        }

        try {
            return resource.getInputStream();
        } catch (FileNotFoundException e) {
            Assert.isTrue(false, "Something is wrong, we should not be here since we know that resource exists.");

            return null;
        } catch (IOException e) {
            logger.error("Error while reading an existing file {}.", resource);

            return null;
        }
    }

    public void setRepositoryRoot(Resource repositoryRoot) {
        Assert.isTrue(repositoryRoot.exists());

        this.repositoryRoot = repositoryRoot;
    }

}
