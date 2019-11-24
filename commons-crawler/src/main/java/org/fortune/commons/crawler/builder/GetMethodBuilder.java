package org.fortune.commons.crawler.builder;

public interface GetMethodBuilder extends HttpMethodBuilder {

    PostMethodBuilder setCredentials(String username, String password);
    
    GetMethodBuilder addGetHeader(String name, String value);

}
