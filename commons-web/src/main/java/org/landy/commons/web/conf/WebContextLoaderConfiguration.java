package org.landy.commons.web.conf;

import org.landy.commons.core.constants.Constants;
import org.landy.commons.web.loader.WebContextLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Landy
 * @date: 2019/4/21 00:18
 * @description:
 */
@Configuration
public class WebContextLoaderConfiguration {
    /**
     * jsp页面跟路径
     */
    @Value("${page.root.path}")
    private String pageRootPath = Constants.SLASH;
    /**
     * 系统包名的前缀
     */
    @Value("${package.name.prefix}")
    private String packageNamePrefix;

    // 配置WebContextLoader
    @Bean(name = WebContextLoader.BEAN_NAME_WEB_CONTEXT_LOADER)
    public WebContextLoader webContextLoader() {
        WebContextLoader webContextLoader = new WebContextLoader();
        webContextLoader.setPageRootPath(pageRootPath);
        webContextLoader.setPackageNamePrefix(packageNamePrefix);
        return webContextLoader;
    }
}
