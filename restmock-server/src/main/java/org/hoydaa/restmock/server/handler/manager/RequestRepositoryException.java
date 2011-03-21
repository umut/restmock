package org.hoydaa.restmock.server.handler.manager;

/**
 * @author Umut Utkan
 */
public class RequestRepositoryException extends RuntimeException {

    public final static int ERROR_EXCEEDED_CALL = 1;

    public static final int ERROR_NOT_SET = 2;


    private int code;


    public RequestRepositoryException(int code, String s) {
        super(s);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
