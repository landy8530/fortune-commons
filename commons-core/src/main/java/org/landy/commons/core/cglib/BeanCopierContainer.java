package org.landy.commons.core.cglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCopierContainer {

    private Logger log= LoggerFactory.getLogger(BeanCopierContainer.class);

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private static final String RESOURCE_PATTERN = "/**/*.class";
    private List<String> packagesToScanList=new ArrayList<String>();
    private static ConcurrentHashMap<String ,BeanCopier> MyBeanCopyContainerMap=new ConcurrentHashMap<>();

    public void init(){
        log.info("创建模型的BeanCopier,start...");
        try {
            for (String pkg : packagesToScanList) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
                Resource[] resources = resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        String className = reader.getClassMetadata().getClassName();
                        Class clazz = Class.forName(className);
                        BeanCopier myBeanCopier = BeanCopier.create(clazz, clazz, false);
                        MyBeanCopyContainerMap.put(className, myBeanCopier);
                    }
                }
            }
            log.info("创建模型的BeanCopier,end;size="+ BeanCopierContainer.MyBeanCopyContainerMap.size());
        }
        catch (IOException ex) {
            log.error("MY BEAN COPY 初始化失败");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private static synchronized BeanCopier createMyBeanCopier(Class clazz){
        BeanCopier beanCopier=MyBeanCopyContainerMap.get(clazz.getName());
        if(beanCopier==null){
            beanCopier=BeanCopier.create(clazz, clazz, false);
            MyBeanCopyContainerMap.put(clazz.getName(), beanCopier);
        }
        return beanCopier;
    }

    public static BeanCopier accessBeanCopier(Class clazz){
        BeanCopier beanCopier=MyBeanCopyContainerMap.get(clazz.getName());
        if(beanCopier==null){
            beanCopier=createMyBeanCopier(clazz);
        }
        return beanCopier;
    }

    public void setPackagesToScanList(List<String> packagesToScanList) {
        this.packagesToScanList = packagesToScanList;
    }
}