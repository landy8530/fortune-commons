package org.fortune.commons.core.http;

/**
 * @author: landy
 * @date: 2019/11/20 21:51
 * @description:
 */
public interface FormHttpRequest {

    void addParameter(String key, String value);

    void setUrl(String url);

}
