package org.fortune.commons.web.springmvc.conf.autoconfigure;

import org.fortune.commons.core.constants.Constants;
import org.fortune.commons.web.springmvc.conf.RootApplicationContextConfiguration;
import org.fortune.commons.web.springmvc.conf.SpringWebMvcConfiguration;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContainerInitializer;

/**
 * Web 自动装配
 * 注意，这个类不需要使用web.xml配置加载，它会由Spring MVC的机制加载，
 * 或许你会惊讶，它为什么会自动加载呢？
 * 那是因为在spring 3.1之后，它给了我们一个类，
 * SpringServletContainerInitializer，那么这个类就继承了Servlet规范的ServletContainerInitializer，
 * 按照Servlet的规范，Java Web容器启动的时候，就会加载实现这个类的方法，
 * 于是在Java Web容器初始化的时候，这个类就会被我们加载进来了。
 * @see SpringServletContainerInitializer
 * @see ServletContainerInitializer
 */
public class WebApplicationAutoInitializer extends
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
        return new Class[]{RootApplicationContextConfiguration.class};
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

}
