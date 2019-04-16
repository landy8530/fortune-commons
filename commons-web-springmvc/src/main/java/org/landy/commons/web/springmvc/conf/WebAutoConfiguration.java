package org.landy.commons.web.springmvc.conf;

import org.landy.commons.core.constants.Constants;
import org.landy.commons.web.conf.WebApplicationContextConfiguration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Web 自动装配
 */
//@EnableWebMvc
//@ComponentScan("org.landy.jsp.in.spring.web.controller")
//@Configuration
public class WebAutoConfiguration extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * <filter>
     * <filter-name>CharacterEncodingFilter</filter-name>
     * <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
     * <init-param>
     * <param-name>encoding</param-name>
     * <param-value>UTF-8</param-value>
     * </init-param>
     * <init-param>
     * <param-name>forceRequestEncoding</param-name>
     * <param-value>true</param-value>
     * </init-param>
     * <init-param>
     * <param-name>forceResponseEncoding</param-name>
     * <param-value>true</param-value>
     * </init-param>
     * </filter>
     * <p>
     * <filter-mapping>
     * <filter-name>CharacterEncodingFilter</filter-name>
     * <servlet-name>app</servlet-name>
     * </filter-mapping>
     */
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{characterEncodingFilter()};
    }

    private Filter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding(Constants.ENCODING_UTF_8);
        filter.setForceRequestEncoding(true);
        filter.setForceResponseEncoding(true);
        return filter;
    }

    /**
     * 等价于 {@link ContextLoaderListener}
     * 即 root WebAPplicationContext，对应配置文件：WEB-INF/app-context.xml
     * @return
     */
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{WebApplicationContextConfiguration.class};
    }

    /***
     * 相当于 DispatcherServlet 加载 WEB-INF/jsp-in-spring.xml 文件
     * @return
     */
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringWebMvcConfiguration.class};
    }

    /**
     * <servlet-mapping>
     * <servlet-name>app</servlet-name>
     * <url-pattern>/</url-pattern>
     * </servlet-mapping>
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected String getServletName() {
        return "app";
    }

    /**
     * 重写创建DispatcherServlet
     * @param servletAppContext
     * @return
     */
//    protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
//        return new FortuneDispatcherServlet(servletAppContext);
//    }
}
