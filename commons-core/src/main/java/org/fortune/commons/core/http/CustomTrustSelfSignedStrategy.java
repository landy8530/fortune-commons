package org.fortune.commons.core.http;

import org.apache.http.conn.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author: landy
 * @date: 2019/11/20 22:33
 * @description:
 */
public class CustomTrustSelfSignedStrategy implements TrustStrategy {
    public static final CustomTrustSelfSignedStrategy INSTANCE = new CustomTrustSelfSignedStrategy();

    public CustomTrustSelfSignedStrategy() {
    }

    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        return true;
    }
}
