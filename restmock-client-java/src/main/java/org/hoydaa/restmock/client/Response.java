package org.hoydaa.restmock.client;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Umut Utkan
 */
public class Response {

    private int status;

    private InputStream stream;

    private String contentPath;

    private String type;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public InputStream getStream() {
        if (null != stream) {
            try {
                stream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

}
