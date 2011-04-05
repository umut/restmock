package org.hoydaa.restmock.server.handler.manager;

/**
 * @author Umut Utkan
 */
public class RequestRepositoryException extends RuntimeException {

    private ResponseStatus status;

    public RequestRepositoryException(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

}
