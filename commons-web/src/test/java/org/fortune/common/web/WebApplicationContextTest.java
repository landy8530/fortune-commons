package org.fortune.common.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.fortune.commons.core.help.BeanInitializeCompletedListener;
import org.fortune.commons.datacache.DataCacheFacade;
import org.fortune.commons.datacache.adapter.CacheDataLoadAdapter;
import org.fortune.commons.web.conf.ApplicationContextConfiguration;
import org.fortune.commons.web.conf.ExportAttachmentHandlerConfiguration;
import org.fortune.commons.web.conf.FreemarkerConfiguration;
import org.fortune.commons.web.conf.WebContextLoaderConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@WebAppConfiguration  //调用Java Web组件，如自动注入ServletContext Bean等
@ContextConfiguration(classes = {
        BeanInitializedCompletedConfiguration.class,
        ApplicationContextConfiguration.class,
        ExportAttachmentHandlerConfiguration.class,
        FreemarkerConfiguration.class,
        WebContextLoaderConfiguration.class,
        DataCachedConfiguration.class
}) //加载Spring配置文件
public class WebApplicationContextTest {

    @Test
    public void test() {
        System.out.println("hello,world!");
    }

}

@Configuration
class DataCachedConfiguration {
    // 配置DataCacheFacade
    @Bean(name = DataCacheFacade.BEAN_NAME_DATA_CACHE_FACADE)
    public DataCacheFacade dataCacheFacade() {
        DataCacheFacade dataCacheFacade = new DataCacheFacade();
        List<CacheDataLoadAdapter> cacheDataAdapterList = new ArrayList<>();
        cacheDataAdapterList.add(new TestCodeCacheDataLoadAdapter());
        dataCacheFacade.setCacheDataAdapterList(cacheDataAdapterList);
        return dataCacheFacade;
    }

    public class TestCodeCacheDataLoadAdapter extends CacheDataLoadAdapter {
        private List<String> keys=new ArrayList<String>();
        @Override
        public boolean loadData() {
            Map<String,Object> dataMap= new HashMap<>();
            Map<String,Object> data = new HashMap<>();
            data.put("chris2","chris1");
            data.put("chris3","chris2");
            data.put("chris4","chris3");
            dataMap.put("landy12",data);
            dataMap.put("landy22","test2");
            dataMap.put("landy32","test3");
            dataMap.put("landy42","test4");
            dataMap.put("landy52","test5");
            for(Map.Entry<String,Object> item1:dataMap.entrySet()){
                keys.add(item1.getKey());
                super.getStoreCacheDataService().store(item1.getKey(),item1.getValue());
            }
            return true;
        }

        @Override
        public List<String> getStoreKeys() {
            return keys;
        }
    }
}

@Configuration
class BeanInitializedCompletedConfiguration {
    @Autowired
    DataCacheFacade dataCacheFacade;

    @Bean
    public BeanInitializeCompletedListener beanInitializeCompletedListener() {
        BeanInitializeCompletedListener beanInitializeCompletedListener = new BeanInitializeCompletedListener();
        List<AbstractApplicationContextHelper> initSysHelperBeanList = new ArrayList<>();
        initSysHelperBeanList.add(dataCacheFacade);
        beanInitializeCompletedListener.setInitSysHelperBeanList(initSysHelperBeanList);
        return beanInitializeCompletedListener;
    }
}
