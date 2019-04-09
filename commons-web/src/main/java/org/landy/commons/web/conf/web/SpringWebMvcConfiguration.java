package org.landy.commons.web.conf.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan("org.landy.commons.web.controller")
@Configuration
public class SpringWebMvcConfiguration {

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

}
