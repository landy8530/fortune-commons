package org.fortune.doc.common.enums;

/**
 * @author: landy
 * @date: 2019/5/30 23:09
 * @description: 文件操作类型枚举
 */
public enum DocOperationType {

    NULL("", ""),

    /**
     * 上传文件
     */
    UPLOAD_FILE("上传文件", "uploadFile"),
    /**
     * 删除文件
     */
    DELETE_FILE("删除文件", "deleteFile"),
    /**
     * 替换文件
     */
    REPLACE_FILE("替换文件", "replaceFile"),
    /**
     * 生成缩略图
     */
    CREATE_THUMB_PICTURE("生成缩略图", "createThumbPicture");

    private String value;
    private String name;

    private DocOperationType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static DocOperationType fromValue(String value) {
        if (UPLOAD_FILE.value.equals(value))
            return UPLOAD_FILE;
        if (DELETE_FILE.value.equals(value))
            return DELETE_FILE;
        if (REPLACE_FILE.value.equals(value))
            return REPLACE_FILE;
        if (CREATE_THUMB_PICTURE.value.equals(value)) {
            return CREATE_THUMB_PICTURE;
        }
        return NULL;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
