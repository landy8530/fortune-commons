package org.fortune.commons.crawler.builder;

public interface PostMethodBuilder extends HttpMethodBuilder {
    /**
     * add HTTP header for post request
     *
     * @param name HTTP header name
     * @param value HTTP header value
     * @return this
     */
    PostMethodBuilder addHeader(String name, String value);

    /**
     * add HTTP post data (allowing redundant)
     * @param name parameter name
     * @param value parameter value
     * @return this
     */
    PostMethodBuilder addParam(String name, String value);

    /**
     * set value for HTTP post data (will overwrite parameter value)
     * @param name parameter name
     * @param value parameter value
     * @return this
     */
    PostMethodBuilder setParamValue(String name, String value);

    /**
     * set url for post request
     * @param url url string
     * @return this
     */
    PostMethodBuilder setUrl(String url);
}
