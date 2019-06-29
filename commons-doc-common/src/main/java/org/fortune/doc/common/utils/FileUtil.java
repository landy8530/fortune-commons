package org.fortune.doc.common.utils;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.account.Account;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author: landy
 * @date: 2019/6/29 12:21
 * @description:
 */
public final class FileUtil {

    /**
     * 动态产生文件上传的目标路径，根据用户的账户配置动态生成
     * @param account
     * @return
     */
    public static final String generateFileSavePath(Account account) {
        StringBuffer buf = new StringBuffer(account.getUserName()).append(File.separator);
        int level = account.getLevel();

        if (level > Constants.FOLDER_MAX_LEVEL) {
            level = Constants.FOLDER_MAX_LEVEL;
        }

        Random r = new Random();
        for (int i = 0; i < level; i++) {
            buf.append(
                    Constants.LETTER_AND_NUMBER_CHAR[r
                            .nextInt(Constants.LETTER_AND_NUMBER_CHAR.length)])
                    .append(File.separator);
        }
        if (buf.charAt(0) == File.separator.charAt(0)) {
            buf.deleteCharAt(0);
        }

        return buf.toString();
    }

    public static final String generateFileNameOfTime(String fileName) {
        DateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);
        int position = fileName.lastIndexOf(".");
        String suffix = fileName.substring(position);
        return formatDate + "_" + random + suffix;
    }
}
