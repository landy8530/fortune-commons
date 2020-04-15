package org.fortune.commons.core.util;

import org.apache.commons.io.FileUtils;
import org.fortune.commons.core.constants.Constants;
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

    /**
     *
     * @param srcFile
     * @param destDir
     * @return return the new File name after moving to the new dir
     */
    public static String moveFileToDirectory(File srcFile, File destDir) {
        String srcFileName = srcFile.getName();
        String destFilePath = destDir.getAbsolutePath() + File.separator + srcFileName;
        File destFile = new File(destFilePath);
        try {
            if(destFile.exists()) {
                //rename
                int i = srcFileName.indexOf(Constants.DELIMITER_PERIOD);
                String existFileName = srcFileName.substring(0, i) + Constants.DELIMITER_UNDERSCORE + System.currentTimeMillis() + srcFileName.substring(i);
                int lastIndex = srcFile.getAbsolutePath().lastIndexOf(File.separator);
                String newSrcFilePath = srcFile.getAbsolutePath().substring(0,lastIndex) + File.separator + existFileName;
                File newSrcFile = new File(newSrcFilePath);
                srcFile.renameTo(newSrcFile);
                FileUtils.moveFileToDirectory(newSrcFile,destDir,true);
                return newSrcFile.getName();
            } else {
                FileUtils.moveFileToDirectory(srcFile,destDir,true);
            }
        } catch (IOException e) {
            LOGGER.error("Move file to directory failure, srcFile:{},destDir:{}",srcFile,destDir,e);
        }
        return srcFileName;
    }
}
