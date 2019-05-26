package org.fortune.commons.web.conf;

import org.fortune.commons.core.freemarker.FortuneFreemarkerResolver;
import org.fortune.commons.web.loader.WebContextLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Landy
 * @date: 2019/4/6 22:47
 * @description:
 */
@Configuration
public class FreemarkerConfiguration {

    @Bean
    public FortuneFreemarkerResolver fortuneFreemarkerResolver() {
        FortuneFreemarkerResolver resolver = FortuneFreemarkerResolver.getInstance();
        //设置根路径
        resolver.setRootPath(WebContextLoader.getInstance().getRootPath());
        resolver.init(); //初始化FTL解析组件
        return resolver;
    }

}
