package org.landy.commons.web.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: Landy
 * @date: 2019/5/7 22:27
 * @description:
 */
@Controller
public class MyMvcController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String prepareView(Model model) {
        model.addAttribute("msg", "Spring quick start!!");
        return "my-page";
    }

}
