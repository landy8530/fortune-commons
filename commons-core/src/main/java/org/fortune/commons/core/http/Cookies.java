package org.fortune.commons.core.http;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/20 20:40
 * @description:
 */
public class Cookies {

    private final CookieStore cookieStore;

    public Cookies(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public void addCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        this.cookieStore.addCookie(cookie);
    }

    public List<Cookie> getCookies() {
        List<org.apache.http.cookie.Cookie> originalCookies = this.cookieStore.getCookies();
        List<Cookie> cookies = new ArrayList(originalCookies.size());
        Iterator iterator = originalCookies.iterator();

        while(iterator.hasNext()) {
            org.apache.http.cookie.Cookie originalCookie = (org.apache.http.cookie.Cookie)iterator.next();
            Cookie cookie = new Cookie(originalCookie.getName(), originalCookie.getValue());
            cookies.add(cookie);
        }

        return cookies;
    }
}
