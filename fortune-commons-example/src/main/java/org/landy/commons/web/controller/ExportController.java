package org.landy.commons.web.controller;

import org.landy.commons.web.export.ExportAttachment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Landy
 * @date: 2019/4/16 22:36
 * @description:
 */
@Controller
public class ExportController {

    @RequestMapping("/exportXls")
    public void exportXls(ModelMap modelMap, HttpServletRequest request) {
        ExportAttachment.generateXls(request,
                ExportAttachment.ContentDisplayType.attachment,"menu.xls",
                "menuListExport.xls");
    }

    @RequestMapping("/exportPdf")
    public void exportPdf(ModelMap modelMap, HttpServletRequest request) {
        ExportAttachment.generatePdf(request,
                ExportAttachment.ContentDisplayType.attachment,"menu.pdf",
                "menuListExport.ftl");
    }
}
