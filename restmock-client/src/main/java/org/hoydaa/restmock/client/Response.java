package org.hoydaa.restmock.client;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.InputStream;

/**
 * @author Umut Utkan
 */
public class Response {

    private int status;

    private InputStream stream;

    private String type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
