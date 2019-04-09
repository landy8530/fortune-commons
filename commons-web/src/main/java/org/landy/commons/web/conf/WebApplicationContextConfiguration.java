package org.landy.commons.web.conf;

import org.landy.commons.datacache.DataCacheFacade;
import org.landy.commons.datacache.adapter.CacheDataLoadAdapter;
import org.landy.commons.web.loader.WebContextLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/7 00:04
 * @description: WebApplicationContext配置类
 */
@Configuration
@Import({ApplicationContextConfiguration.class})
public class WebApplicationContextConfiguration {
    /**
     * jsp页面跟路径
     */
    @Value("${page.root.path}")
    private String pageRootPath = "/";
    /**
     * 系统包名的前缀
     */
    @Value("${package.name.prefix}")
    private String packageNamePrefix;

    // 配置DataCacheFacade
    @Bean(name = DataCacheFacade.BEAN_NAME_DATA_CACHE_FACADE)
    public DataCacheFacade dataCacheFacade() {
        DataCacheFacade dataCacheFacade = new DataCacheFacade();
        List<CacheDataLoadAdapter> cacheDataAdapterList = new ArrayList<>();
        cacheDataAdapterList.add(new CodeCacheDataLoadAdapter());
        dataCacheFacade.setCacheDataAdapterList(cacheDataAdapterList);
        return dataCacheFacade;
    }

    public class CodeCacheDataLoadAdapter extends CacheDataLoadAdapter {
        private List<String> keys=new ArrayList<String>();
        @Override
        public boolean loadData() {
            Map<String,Object> dataMap= new HashMap<>();
            Map<String,Object> data = new HashMap<>();
            data.put("chris1","chris1");
            data.put("chris2","chris2");
            data.put("chris3","chris3");
            dataMap.put("landy1",data);
            dataMap.put("landy2","test2");
            dataMap.put("landy3","test3");
            dataMap.put("landy4","test4");
            dataMap.put("landy5","test5");
            for(Map.Entry<String,Object> item:dataMap.entrySet()){
                keys.add(item.getKey());
                super.getStoreCacheDataService().store(item.getKey(),item.getValue());
            }
            return true;
        }

        @Override
        public List<String> getStoreKeys() {
            return keys;
        }
    }

    // 配置WebContextLoader
    @Bean(name = WebContextLoader.BEAN_NAME_WEB_CONTEXT_LOADER)
    public WebContextLoader webContextLoader() {
        WebContextLoader webContextLoader = new WebContextLoader();
        webContextLoader.setPageRootPath(pageRootPath);
        webContextLoader.setPackageNamePrefix(packageNamePrefix);
        return webContextLoader;
    }

}
