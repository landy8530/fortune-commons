package org.fortune.commons.web.springmvc.interceptor;

import org.fortune.commons.web.export.ExportAttachmentHandler;
import org.fortune.commons.web.export.ExportUtils;
import org.fortune.commons.web.util.PathUtils;
import org.fortune.commons.web.springmvc.form.BaseForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: Landy
 * @date: 2019/4/20 13:17
 * @description:
 */
public class UserExportInterceptor implements HandlerInterceptor {
    private final Logger LOGGER = LoggerFactory.getLogger(UserExportInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //如果是导出,或返回NONE值的情况不进行后续处理
        boolean isExport = ExportUtils.isExport(request);
        if (isExport) {
            Object returnValue = null;
            if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
                HandlerMethod newHandler = ((HandlerMethod) handler);
                Class returnType = newHandler.getMethod().getReturnType();
                String className = newHandler.getBeanType().getSimpleName();

                //返回值为空的情况下
                BaseForm form = null;
                ModelMap modelMap = modelAndView.getModelMap();
                if (newHandler.isVoid() && className.contains("Controller")) {
                    String methodName = newHandler.getMethod().getName();
//                    if (args != null) {
//                        for (Object item : args) {
//                            if (item instanceof BaseForm) {
//                                form = (BaseForm) item;
//                            } else if (item instanceof ModelMap) {
//                                modelMap = (ModelMap) item;
//                            }
//                        }
//                        if (modelMap != null && form != null) {
//                            InvokerFormPropertyValueToModelMapUtil.invokerPropertyValToModelMap(form, modelMap);
//                        }
//                    }
                    returnValue = PathUtils.getJSPPagePath(newHandler.getBeanType(), methodName);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("跳转的JSP路径：" + returnValue);
                    }
                }
                //导出文件的判断
                if (returnValue != null) {
                    export(request, returnValue.toString(), modelMap, response);
                }
            }
        }
    }

    private void export(HttpServletRequest request, String fileName, ModelMap data, HttpServletResponse response) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("导出操作文件--start");
        }
        ExportAttachmentHandler.getInstance().export(request, response,
                fileName, data);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("导出操作文件--end");
        }
    }
}
