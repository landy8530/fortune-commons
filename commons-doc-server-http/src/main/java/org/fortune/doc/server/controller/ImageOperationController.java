package org.fortune.doc.server.controller;

import org.fortune.doc.common.domain.Constants;
import org.fortune.doc.common.domain.result.ImageDocResult;
import org.fortune.doc.server.handler.image.DeleteImageServerHandler;
import org.fortune.doc.server.handler.image.ReplaceImageServerHandler;
import org.fortune.doc.server.handler.image.UploadImageServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: landy
 * @date: 2019/6/26 22:21
 * @description:
 */
@Controller
public class ImageOperationController {

    @Autowired
    private UploadImageServerHandler docUploadServerHandler;
    @Autowired
    private ReplaceImageServerHandler docReplaceServerHandler;
    @Autowired
    private DeleteImageServerHandler docDeleteServerHandler;

    @RequestMapping(
            value = {Constants.URL_UPLOAD_IMAGE},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public ImageDocResult uploadImageDoc(HttpServletRequest request) {
        ImageDocResult result = this.docUploadServerHandler.doUpload(request);
        return result;
    }

    @RequestMapping(
            value = {Constants.URL_REPLACE_IMAGE},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public ImageDocResult replaceImageDoc(HttpServletRequest request) {
        ImageDocResult result = this.docReplaceServerHandler.doReplace(request);
        return result;
    }

    @RequestMapping(
            value = {Constants.URL_DELETE_IMAGE},
            method = {RequestMethod.POST}
    )
    @ResponseBody
    public ImageDocResult deleteImageDoc(HttpServletRequest request) {
        ImageDocResult result = this.docDeleteServerHandler.doDelete(request);
        return result;
    }

}
