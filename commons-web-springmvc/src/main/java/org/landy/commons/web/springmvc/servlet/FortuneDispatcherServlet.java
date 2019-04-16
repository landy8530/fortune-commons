package org.landy.commons.web.springmvc.servlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Landy
 * @date: 2019/4/16 23:26
 * @description:
 */
public class FortuneDispatcherServlet extends DispatcherServlet {

    public FortuneDispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {

//        Locale locale = this.localeResolver.resolveLocale(request);
//        response.setLocale(locale);
//
//        View view;
//        if (mv.isReference()) {
//            // We need to resolve the view name.
//            view = resolveViewName(mv.getViewName(), mv.getModelMap(), locale, request);
//            if (view == null) {
//                throw new ServletException(
//                        "Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" +
//                                getServletName() + "'");
//            }
//        }
//        else {
//            // No need to lookup: the ModelAndView object contains the actual View object.
//            view = mv.getView();
//            if (view == null) {
//                throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a " +
//                        "View object in servlet with name '" + getServletName() + "'");
//            }
//        }
//
//        // Delegate to the View object for rendering.
//        if (logger.isDebugEnabled()) {
//            logger.debug("Rendering view [" + view + "] in DispatcherServlet with name '" + getServletName() + "'");
//        }
//        //如果是导出,或返回NONE值的情况不进行后续处理
//        boolean bool=!(ExportAttachParamHelper.isExport(request))
//                ||MyWebStaticConstants.NONE.equals(request.getParameter(MyWebStaticConstants.NONE));
//
//        if(bool){
//            view.render(mv.getModelMap(), request, response);
//        }

    }

}
