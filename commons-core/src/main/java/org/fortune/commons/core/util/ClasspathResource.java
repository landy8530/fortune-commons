package org.fortune.commons.core.util;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author: landy
 * @date: 2019/11/20 23:40
 * @description:
 */
public class ClasspathResource {

    private byte[] bytes;
    private String resourcePath;

    public ClasspathResource(String resourcePath) {
        this.resourcePath = resourcePath;
        InputStream stream = null;

        try {
            stream = ClasspathResource.class.getClassLoader().getResourceAsStream(resourcePath);
            if (stream == null) {
                throw new IllegalArgumentException("can not load resource, path=" + resourcePath);
            }

            this.bytes = IOUtils.bytes(stream);
        } finally {
            IOUtils.close(stream);
        }

    }

    public ClasspathResource(File resourceFile) {
        FileInputStream stream = null;

        try {
            this.resourcePath = resourceFile.getCanonicalPath();
            stream = new FileInputStream(resourceFile);
            if (stream == null) {
                throw new IllegalArgumentException("can not load resource, path=" + this.resourcePath);
            }

            this.bytes = IOUtils.bytes(stream);
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            IOUtils.close(stream);
        }

    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public String getTextContent() {
        return new String(this.bytes, Charset.defaultCharset());
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String resourcePath() {
        return this.resourcePath;
    }

}
