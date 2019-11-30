package org.fortune.commons.crawler.builder;

public interface GetMethodBuilder extends HttpMethodBuilder {

    PostMethodBuilder setCredentials(String username, String password);
    
    GetMethodBuilder addGetHeader(String name, String value);

    /**
     * add HTTP get data
     * @param name parameter name
     * @param value parameter value
     * @return this
     */
    GetMethodBuilder addGetParam(String name, String value);
}
