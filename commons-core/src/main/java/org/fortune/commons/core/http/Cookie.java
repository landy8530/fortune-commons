package org.fortune.commons.core.http;

/**
 * @author: landy
 * @date: 2019/11/20 20:40
 * @description:
 */
public class Cookie {

    private final String name;
    private final String value;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

}
