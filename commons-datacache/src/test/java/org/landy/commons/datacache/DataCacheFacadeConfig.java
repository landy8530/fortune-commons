package org.landy.commons.datacache;

import org.landy.commons.core.help.AbstractApplicationContextHelper;
import org.landy.commons.core.help.BeanInitializeCompletedListener;
import org.landy.commons.datacache.adapter.CacheDataLoadAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
//@ComponentScan("org.landy.commons.datacache")
public class DataCacheFacadeConfig {

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

}
