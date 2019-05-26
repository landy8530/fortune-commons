package org.fortune.commons.web.loader;

import org.fortune.commons.core.constants.Constants;
import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @author: Landy
 * @date: 2019/4/6 23:23
 * @description: 获取上下文信息的类
 */
public class WebContextLoader extends AbstractApplicationContextHelper implements ServletContextAware {
    public static final String BEAN_NAME_WEB_CONTEXT_LOADER = "webContextLoader";
    private Logger LOGGER = LoggerFactory.getLogger(WebContextLoader.class);
    private ServletContext servletContext;

    /**
     * 模板文件位置
     */
    private String templatePath;
    /**
     * jsp页面跟路径
     */
    private String pageRootPath = Constants.SLASH;
    /**
     * 系统包名的前缀
     */
    private String packageNamePrefix;

    public WebContextLoader() {
    }

    public static WebContextLoader getInstance() {
        return getBean(BEAN_NAME_WEB_CONTEXT_LOADER, WebContextLoader.class);
    }

    /**
     * 得到Web上下文的ServletContext
     *
     * @return
     * @author: Landy
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 获得上下文的路径
     *
     * @return
     * @author: Landy
     */
    public String getContextPath() {
        return servletContext.getServletContextName();
    }

    /**
     * 根据文件名获得实际的路径
     *
     * @param path
     * @return
     * @author: Landy
     */
    public String getRealPath(String path) {
        return servletContext.getRealPath(path);
    }

    public void init() {
        LOGGER.info("开始系统初始化...");
        LOGGER.info(System.getProperty("contextConfigLocation"));
        //设置日志输出变量，以便LOG4J输入日志文件都在logs下
        System.setProperty("MY_LOG_ROOT", ".");
        // 启动其他相关服务
        otherInit();
        LOGGER.info("系统初始化完毕!");
    }

    /**
     * 定义了一个空操作的方法，继承的类覆盖该方法，完成其他的初始化操作。
     */
    protected void otherInit() {
    }

    /**
     * 获取WebRoot的文件目录路径，以/结尾
     *
     * @return 绝对路径
     */
    public String getRootPath() {
        String rootPath = getRealPath(Constants.SLASH);
        rootPath = rootPath.replaceAll("\\\\", Constants.SLASH);
        rootPath += Constants.SLASH;
        return rootPath;
    }

    public void setServletContext(ServletContext servletContext) {
        LOGGER.info("初始化Web上下文...ServletContext=" + servletContext);
        this.servletContext = servletContext;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getPageRootPath() {
        return pageRootPath;
    }

    public void setPageRootPath(String pageRootPath) {
        this.pageRootPath = pageRootPath;
    }

    public String getPackageNamePrefix() {
        return packageNamePrefix;
    }

    public void setPackageNamePrefix(String packageNamePrefix) {
        this.packageNamePrefix = packageNamePrefix;
    }
}
