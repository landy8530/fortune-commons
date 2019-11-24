package org.fortune.commons.core.util;

import org.fortune.commons.core.exception.RuntimeIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * @author: landy
 * @date: 2019/11/20 23:41
 * @description:
 */
public class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);
    private static final int BUFFER_SIZE = 4096;

    public static void write(File file, byte[] bytes) {
        BufferedOutputStream stream = null;

        try {
            stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(bytes);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            close((OutputStream)stream);
        }

    }

    public static void write(File file, String content) {
        write(file, content.getBytes(Charset.defaultCharset()));
    }

    public static String text(File file) {
        BufferedInputStream stream = null;

        String value;
        try {
            stream = new BufferedInputStream(new FileInputStream(file));
            value = new String(bytes((InputStream)stream), Charset.defaultCharset());
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            close((InputStream)stream);
        }

        return value;
    }

    public static String text(InputStream stream) {
        return new String(bytes(stream), Charset.defaultCharset());
    }

    public static String textOfGZIP(File gzfile) {
        GZIPInputStream stream = null;

        String value;
        try {
            stream = new GZIPInputStream(new FileInputStream(gzfile));
            value = new String(bytes((InputStream)stream), Charset.defaultCharset());
        } catch (FileNotFoundException ex) {
            throw new RuntimeIOException(ex);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            close((InputStream)stream);
        }

        return value;
    }

    public static byte[] bytes(File file) {
        BufferedInputStream stream = null;

        byte[] value;
        try {
            stream = new BufferedInputStream(new FileInputStream(file));
            value = bytes((InputStream)stream);
        } catch (FileNotFoundException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            close((InputStream)stream);
        }

        return value;
    }

    public static byte[] bytes(InputStream stream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];

        try {
            while(true) {
                int len = stream.read(buf);
                if (len < 0) {
                    return byteArrayOutputStream.toByteArray();
                }

                byteArrayOutputStream.write(buf, 0, len);
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static void close(InputStream stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    public static void close(OutputStream stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    public static void close(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    public static void close(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

    }

    public static String getTempFolderPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static void copy(File from, File to) {
        InputStream input = null;
        BufferedOutputStream output = null;

        try {
            input = new BufferedInputStream(new FileInputStream(from));
            output = new BufferedOutputStream(new FileOutputStream(to));
            byte[] buf = new byte[4096];

            while(true) {
                int len = input.read(buf);
                if (len < 0) {
                    return;
                }

                output.write(buf, 0, len);
            }
        } catch (IOException var9) {
            throw new RuntimeIOException(var9);
        } finally {
            close((InputStream)input);
            close((OutputStream)output);
        }
    }

    private IOUtils() {
    }
    
}
