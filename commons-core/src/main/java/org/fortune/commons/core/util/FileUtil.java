package org.fortune.commons.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: landy
 * @date: 2020/3/25 09:08
 * @description:
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static String getFilePathByClassPath(String filePath) {
        java.net.URL url = FileUtil.class.getResource(filePath);
        if(url == null) {
            url = FileUtil.class.getClassLoader().getResource(filePath);
            if(url == null) {
                url = Thread.currentThread().getContextClassLoader().getResource(filePath);
            }
        }
        String path = url.getFile();
        return path;
    }

    /**
     * byte 转file
     */
    public static File byte2File(byte[] buf, String filePath, String fileName){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()){
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            LOGGER.error("Occurring an unexpected exception", e);
        } finally {
            if (bos != null){
                try{
                    bos.close();
                } catch (IOException e){
                    LOGGER.error("Occurring an unexpected exception", e);
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    LOGGER.error("Occurring an unexpected exception", e);
                }
            }
        }
        return file;
    }

}
