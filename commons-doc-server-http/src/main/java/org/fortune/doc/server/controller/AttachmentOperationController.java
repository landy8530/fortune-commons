package org.fortune.doc.server.controller;

import org.fortune.doc.common.domain.result.AttachDocResult;
import org.fortune.doc.common.enums.DocOperationURI;
import org.fortune.doc.server.handler.attachment.DeleteAttachmentServerHandler;
import org.fortune.doc.server.handler.attachment.ReplaceAttachmentServerHandler;
import org.fortune.doc.server.handler.attachment.UploadAttachmentServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: landy
 * @date: 2019/6/26 22:22
 * @description:
 */
@Controller
public class AttachmentOperationController {

    @Autowired
    private UploadAttachmentServerHandler docUploadServerHandler;
    @Autowired
    private ReplaceAttachmentServerHandler docReplaceServerHandler;
    @Autowired
    private DeleteAttachmentServerHandler docDeleteServerHandler;

    @RequestMapping(
            value = {DocOperationURI.URL_UPLOAD_ATTACH},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public AttachDocResult uploadAttachDoc(HttpServletRequest request) {
        AttachDocResult result = this.docUploadServerHandler.doUpload(request);
        return result;
    }

    @RequestMapping(
            value = {DocOperationURI.URL_REPLACE_ATTACH},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public AttachDocResult replaceAttachDoc(HttpServletRequest request) {
        AttachDocResult result = this.docReplaceServerHandler.doReplace(request);
        return result;
    }

    @RequestMapping(
            value = {DocOperationURI.URL_DELETE_ATTACH},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public AttachDocResult deleteAttachDoc(HttpServletRequest request) {
        AttachDocResult result = this.docDeleteServerHandler.doDelete(request);
        return result;
    }

}
