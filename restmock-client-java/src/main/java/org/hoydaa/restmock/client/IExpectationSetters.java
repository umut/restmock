package org.hoydaa.restmock.client;


import org.hoydaa.restmock.model.Method;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Umut Utkan
 */
public interface IExpectationSetters {

    IExpectationSetters expect(String path, Method method);

    IExpectationSetters withParam(String key, String[] value);

    IExpectationSetters withHeader(String key, String value);

    IExpectationSetters andReply(String type, int code, String rtn) throws IOException;

    IExpectationSetters andReply(String type, int code, InputStream rtn) throws IOException;

    IExpectationSetters times(int times);

    IExpectationSetters anyTimes();

    void replay();

    IExpectationSetters reset();

}
