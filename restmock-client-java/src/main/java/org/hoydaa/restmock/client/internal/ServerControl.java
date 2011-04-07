package org.hoydaa.restmock.client.internal;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.hoydaa.restmock.client.IExpectationSetters;
import org.hoydaa.restmock.client.IServerControl;
import org.hoydaa.restmock.client.model.Method;
import org.hoydaa.restmock.client.model.Request;
import org.hoydaa.restmock.client.model.Server;
import org.hoydaa.restmock.client.util.Assert;
import org.hoydaa.restmock.client.util.StreamDeserializer;
import org.hoydaa.restmock.client.util.StreamSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Umut Utkan
 */
public class ServerControl implements IServerControl, IExpectationSetters {

    private final static Logger logger = LoggerFactory.getLogger(ServerControl.class);

    private static final String MANAGEMENT_SERVICE = "manage";


    private ServerState state;

    private Request lastRequest;


    @Override
    public IExpectationSetters defineServer(String uri) {
        try {
            URI r = new URI(uri);
            state = new ServerState(uri);

            return this;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Given uri '" + uri + "' is not a valid URI.");
        }
    }

    @Override
    public IExpectationSetters expect(String path, Method method) {
        if (null != lastRequest) {
            addRequestToControl();
        }

        lastRequest = new Request();
        lastRequest.setMethod(method);
        lastRequest.setPath(path);

        return this;
    }

    @Override
    public IExpectationSetters withParam(String key, String[] value) {
        Assert.assertNotNull(lastRequest, "You have to call expect before calling withParam.");

        lastRequest.getParams().put(key, value);

        return this;
    }

    @Override
    public IExpectationSetters withHeader(String key, String value) {
        Assert.assertNotNull(lastRequest, "You have to call expect before withHeader.");

        lastRequest.getHeaders().put(key, value);

        return this;
    }

    @Override
    public IExpectationSetters andReply(String type, int code, String rtn) throws IOException {
        Assert.assertNotNull(lastRequest, "You have to call expect before andReply.");

        if (rtn.startsWith("local:")) {
            return andReply(type, code, FileUtils.openInputStream(new File(rtn.substring(6))));
        } else if (rtn.startsWith("remote:")) {
            lastRequest.getResponse().setContentPath(rtn);
            return andReply(type, code, (InputStream) null);
        } else if (rtn.startsWith("classpath:")) {
            return andReply(type, code, getClass().getResourceAsStream(rtn.substring(10)));
        } else if (rtn.startsWith("http")) {
            lastRequest.getResponse().setContentPath(rtn);
            return andReply(type, code, (InputStream) null);
        } else {
            return andReply(type, code, IOUtils.toInputStream(rtn, "UTF-8"));
        }
    }

    @Override
    public IExpectationSetters andReply(String type, int code, InputStream stream) throws IOException {
        lastRequest.getResponse().setStatus(code);
        lastRequest.getResponse().setType(type);
        lastRequest.getResponse().setStream(stream);

        return this;
    }

    @Override
    public IExpectationSetters times(int times) {
        Assert.assertTrue(lastRequest.getTimes() != 0, "You cannot call times, anyTimes more than once per request.");

        lastRequest.setTimes(times);

        return this;
    }

    @Override
    public IExpectationSetters anyTimes() {
        Assert.assertTrue(lastRequest.getTimes() != 0, "You cannot call times, anyTimes more than once per request.");

        lastRequest.setTimes(-1);

        return this;
    }

    @Override
    public void replay() {
        if (null != lastRequest) {
            addRequestToControl();
        }

        try {
            Server server = getServer();
            String json = server.toJsonString();

            logger.info("About to post expectations\n{}", json);

            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(server.getUrl() + "/" + MANAGEMENT_SERVICE);
            postMethod.setRequestBody(json);
            int code = httpClient.executeMethod(postMethod);

            if (code != 200) {
                if (code == 400) {
                    logger.error("Error while setting expectations due to problematic input.");
                }
                throw new RuntimeException("Error while setting expectations, return code is " + code);
            }

            logger.info("Expectations are successfully set.");
        } catch (IOException e) {

        }
    }

    @Override
    public IExpectationSetters reset() {
        try {
            logger.info("About to reset expectations\n{}");

            Server server = getServer();
            HttpClient httpClient = new HttpClient();
            DeleteMethod deleteMethod = new DeleteMethod(server.getUrl() + "/" + MANAGEMENT_SERVICE);
            int code = httpClient.executeMethod(deleteMethod);

            if (code != 200) {
                throw new RuntimeException("Error while resetting expectations, return code is " + code);
            }

            logger.info("Expectations are successfully reset.");
        } catch (IOException e) {

        }

        return this;
    }

    @Override
    public IExpectationSetters configure(String json) throws IOException {
        state = new ServerState(new Server().configure(json));

        return this;
    }

    public Server getServer() {
        return state.getServer();
    }

    public IExpectationSetters finish() {
        if (null != lastRequest) {
            addRequestToControl();
        }

        return this;
    }

    private void addRequestToControl() {
        state.expect(lastRequest);

        lastRequest = null;
    }

}
