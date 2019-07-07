package org.fortune.doc.client;

import org.apache.http.util.Args;
import org.fortune.doc.client.handler.attachment.DeleteAttachementClientHandler;
import org.fortune.doc.client.handler.attachment.ReplaceAttachmentClientHandler;
import org.fortune.doc.client.handler.attachment.UploadAttachmentClientHandler;
import org.fortune.doc.client.handler.image.DeleteImageClientHandler;
import org.fortune.doc.client.handler.image.ReplaceImageClientHandler;
import org.fortune.doc.client.handler.image.UploadImageClientHandler;
import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.result.AttachDocResult;
import org.fortune.doc.common.domain.result.ImageDocResult;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/7/5 23:57
 * @description:
 */
public class DocClient {

    public static String doImageUpload(String fileName, File file) {
        Args.notNull(fileName, "文件名不为空");
        UploadImageClientHandler uploadHandler = new UploadImageClientHandler();
        ImageDocResult result = uploadHandler.doUploadImage(fileName, file, null);
        return result.getFilePath();
    }

    public static String doImageUploadAndCreateThumb(String fileName, File file) {
        Args.notNull(fileName, "文件名不为空");
        UploadImageClientHandler uploadHandler = new UploadImageClientHandler();
        ImageDocResult result = uploadHandler.doUploadImage(fileName, file, Constants.THUMB_MARK_VAL);
        return result.getFilePath();
    }

    public static String doImageUpload(String fileName, byte[] content) {
        Args.notNull(fileName, "文件名不为空");
        UploadImageClientHandler uploadHandler = new UploadImageClientHandler();
        ImageDocResult result = uploadHandler.doUploadImage(fileName, content, null);
        return result.getFilePath();
    }

    public static String doImageUploadAndCreateThumb(String fileName, byte[] content) {
        Args.notNull(fileName, "文件名不为空");
        UploadImageClientHandler uploadHandler = new UploadImageClientHandler();
        ImageDocResult result = uploadHandler.doUploadImage(fileName, content, Constants.THUMB_MARK_VAL);
        return result.getFilePath();
    }

    public static void doImageReplace(String filePath, byte[] content) {
        Args.notNull(filePath, "原始文件路径不为空");
        ReplaceImageClientHandler operHandler = new ReplaceImageClientHandler();
        operHandler.doReplaceImage(filePath, content);
    }

    public static void doImageReplace(String filePath, File file) {
        Args.notNull(filePath, "原始文件路径不为空");
        ReplaceImageClientHandler operHandler = new ReplaceImageClientHandler();
        operHandler.doReplaceImage(filePath, file);
    }

    public static void doImageDelete(String filePath) {
        Args.notNull(filePath, "原始文件路径不为空");
        DeleteImageClientHandler operHandler = new DeleteImageClientHandler();
        operHandler.doDelete(filePath);
    }

    public static String doAttachUpload(String fileName, File file) {
        Args.notNull(fileName, "文件名不为空");
        UploadAttachmentClientHandler uploadHandler = new UploadAttachmentClientHandler();
        AttachDocResult result = uploadHandler.doUploadDoc(fileName, file, null);
        return result.getFilePath();
    }

    public static String doAttachUpload(String fileName, byte[] content) {
        Args.notNull(fileName, "文件名不为空");
        UploadAttachmentClientHandler uploadHandler = new UploadAttachmentClientHandler();
        AttachDocResult result = uploadHandler.doUploadDoc(fileName, content, null);
        return result.getFilePath();
    }

    public static void doAttachReplace(String filePath, byte[] content) {
        Args.notNull(filePath, "原始文件路径不为空");
        ReplaceAttachmentClientHandler operHandler = new ReplaceAttachmentClientHandler();
        operHandler.doReplaceDoc(filePath, content);
    }

    public static void doAttachReplace(String filePath, File file) {
        Args.notNull(filePath, "原始文件路径不为空");
        ReplaceAttachmentClientHandler operHandler = new ReplaceAttachmentClientHandler();
        operHandler.doReplaceDoc(filePath, file);
    }

    public static void doAttachDelete(String filePath) {
        Args.notNull(filePath, "原始文件路径不为空");
        DeleteAttachementClientHandler operHandler = new DeleteAttachementClientHandler();
        operHandler.doDelete(filePath);
    }

}
