package org.fortune.commons.core.http;

/**
 * @author: landy
 * @date: 2019/11/19 23:04
 * @description:
 */
public class HttpHeader {
    private final String name;
    private final String value;

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
