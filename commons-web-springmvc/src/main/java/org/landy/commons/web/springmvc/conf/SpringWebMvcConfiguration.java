package org.landy.commons.web.springmvc.conf;

import org.landy.commons.core.setting.Settings;
import org.landy.commons.web.conf.ExportAttachmentHandlerConfiguration;
import org.landy.commons.web.conf.FreemarkerConfiguration;
import org.landy.commons.web.conf.WebContextLoaderConfiguration;
import org.landy.commons.web.springmvc.interceptor.UserExportInterceptor;
import org.landy.commons.web.springmvc.load.ControllerConfigurationLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置类继承WebMvcConfigurationSupport或WebMvcConfigurerAdapter类，重写addInterceptors，InterceptorRegistry实例就可以直接添加。
 * 顺便说下继承WebMvcConfigurationSupport或WebMvcConfigurerAdapter的区别，
 * 继承WebMvcConfigurationSupport不需要声明@EnableWebMvc注解，
 * 继承WebMvcConfigurerAdapter需要
 */
@EnableWebMvc // 启用 SpringMVC ，相当于 xml中的 <mvc:annotation-driven/>
@ComponentScan("org.landy.commons.web.controller")
@Configuration
@Import({
        FreemarkerConfiguration.class, // Freemarker component configuration
        ExportAttachmentHandlerConfiguration.class, //Export component
        WebContextLoaderConfiguration.class //Web context loader component
})
public class SpringWebMvcConfiguration implements WebMvcConfigurer /**WebMvcConfigurerAdapter */
{
    private static final String CONTROLLER_PACKAGE_LIST = "controller.packages";
    @Autowired
    private Settings settings;
    private List<String> basePackage;

    /**
     * 设置由 web容器处理静态资源 ，相当于 xml中的<mvc:default-servlet-handler/>
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
     * <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />
     * <property name="prefix" value="/WEB-INF/jsp/"/>
     * <property name="suffix" value=".jsp"/>
     * </bean>
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver
                = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * 初始化RequestMappingHandlerAdapter，并加载Http的Json转换器
     * @return  RequestMappingHandlerAdapter 对象
     */
    @Bean(name="requestMappingHandlerAdapter")
    public HandlerAdapter initRequestMappingHandlerAdapter() {
        //创建RequestMappingHandlerAdapter适配器
        RequestMappingHandlerAdapter rmhd = new RequestMappingHandlerAdapter();
        // HTTP JSON转换器
        MappingJackson2HttpMessageConverter jsonConverter
                = new MappingJackson2HttpMessageConverter();
        //MappingJackson2HttpMessageConverter接收JSON类型消息的转换
        MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(mediaType);
        //加入转换器的支持类型
        jsonConverter.setSupportedMediaTypes(mediaTypes);
        //往适配器加入json转换器
        rmhd.getMessageConverters().add(jsonConverter);
        return rmhd;
    }

    //配置预加载所有Controller所在的包
    //Action 路径扫描加载，Action Form 的属性预加载
    @Bean
    public ControllerConfigurationLoader controllerConfigurationLoader() {
        this.basePackage = settings.getAsList(CONTROLLER_PACKAGE_LIST);
        ControllerConfigurationLoader controllerConfigurationLoader = new ControllerConfigurationLoader(basePackage);
        return controllerConfigurationLoader;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserExportInterceptor()); //用户excel/pdf导出拦截器
    }
}
