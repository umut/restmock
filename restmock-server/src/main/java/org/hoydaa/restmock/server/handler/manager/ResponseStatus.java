package org.hoydaa.restmock.server.handler.manager;

/**
 * Represents the HTTP statuses --those are related to mock handler-- that will be returned by mock handler.
 *
 * @author Umut Utkan
 */
public enum ResponseStatus {

    NOT_READY(901, "Expectations not set"),
    UNEXPECTED_CALL(901, "Unexpected call"),
    EXCEEDED_CALL(902, "Exceeded call"),
    HEADER_MISMATCH(903, "Header mismatch"),
    PARAM_MISMATCH(904, "Request parameter mismatch"),
    REQUEST_MISMATCH(905, "Request mismatch");


    private int code;

    private String explanation;


    ResponseStatus(int code, String explanation) {
        this.code = code;
        this.explanation = explanation;
    }


    public int getCode() {
        return code;
    }

    public String getExplanation() {
        return explanation;
    }

}
