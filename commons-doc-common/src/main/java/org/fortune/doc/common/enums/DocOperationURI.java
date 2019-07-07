package org.fortune.doc.common.enums;

/**
 * @author: landy
 * @date: 2019/7/7 09:44
 * @description:
 */
public enum DocOperationURI {

    IMAGE_UPLOAD(DocOperationType.UPLOAD_FILE, DocOperationURI.URL_UPLOAD_IMAGE),
    IMAGE_REPLACE(DocOperationType.REPLACE_FILE, DocOperationURI.URL_REPLACE_IMAGE),
    IMAGE_DELETE(DocOperationType.DELETE_FILE, DocOperationURI.URL_DELETE_IMAGE),

    ATTACHMENT_UPLOAD(DocOperationType.UPLOAD_FILE, DocOperationURI.URL_UPLOAD_ATTACH),
    ATTACHMENT_REPLACE(DocOperationType.REPLACE_FILE, DocOperationURI.URL_REPLACE_ATTACH),
    ATTACHMENT_DELETE(DocOperationType.DELETE_FILE, DocOperationURI.URL_DELETE_ATTACH),


    ;

    public static final String URL_UPLOAD_IMAGE = "uploadImageDoc.do";
    public static final String URL_REPLACE_IMAGE = "replaceImageDoc.do";
    public static final String URL_DELETE_IMAGE = "deleteImageDoc.do";
    public static final String URL_UPLOAD_ATTACH = "uploadAttachDoc.do";
    public static final String URL_REPLACE_ATTACH = "replaceAttachDoc.do";
    public static final String URL_DELETE_ATTACH = "deleteAttachDoc.do";

    private DocOperationType docOperationType;
    private String uri;

    DocOperationURI(DocOperationType docOperationType, String uri) {
        this.docOperationType = docOperationType;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

}
