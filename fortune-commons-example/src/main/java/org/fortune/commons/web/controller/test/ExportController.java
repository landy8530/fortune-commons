package org.fortune.commons.web.controller.test;

import org.fortune.commons.web.domain.Menu;
import org.fortune.commons.web.export.ExportAttachment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Landy
 * @date: 2019/4/16 22:36
 * @description:
 */
@Controller
public class ExportController {

    @RequestMapping("/exportXls")
    public void exportXls(ModelMap modelMap, HttpServletRequest request) {
        Menu menu = new Menu();
        menu.setMenuName("Landy");
        menu.setUrl("https://github.com/landy8530/fortune-commons");
        menu.setSortNo(1l);

        Menu childMenu = new Menu();
        childMenu.setMenuName("Eva00");
        childMenu.setUrl("https://github.com/landy8530/fortune-commons");
        childMenu.setSortNo(10l);
        Set<Menu> childMenus = new HashSet<>();
        childMenus.add(childMenu);

        childMenu = new Menu();
        childMenu.setMenuName("Eva01");
        childMenu.setUrl("https://github.com/landy8530/fortune-commons");
        childMenu.setSortNo(11l);
        childMenus.add(childMenu);

        menu.setChildMenus(childMenus);

        modelMap.addAttribute("menu",menu);

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
