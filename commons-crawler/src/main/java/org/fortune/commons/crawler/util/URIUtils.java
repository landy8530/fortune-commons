package org.fortune.commons.crawler.util;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

public class URIUtils {
    public static URI createURI(URI lastURI, String newPath) {
        String urlPath;
        if (newPath.toLowerCase().startsWith("http")) {
            urlPath = newPath;
        } else if (newPath.startsWith("/")) {
            urlPath = lastURI.getScheme() + "://" + lastURI.getAuthority() + newPath;
        } else {
            urlPath = lastURI.getScheme() + "://" + lastURI.getAuthority() + lastURI.getPath().substring(0, lastURI.getPath().lastIndexOf("/") + 1) + newPath;
        }

        URI uri = null;

        try {
            URL url = new URL(URLDecoder.decode(urlPath));
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        } catch (Exception e) {
            System.out.println("uri create error. [Exception: ]" + e.getMessage());
        }

        return uri;
    }
}
