package org.fortune.commons.web.springmvc.load;

import org.fortune.commons.web.springmvc.form.BaseForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @author: Landy
 * @date: 2019/4/20 15:45
 * @description:
 */
public final class ControllerFormPropertyLoader {

    private static Logger LOGGER = LoggerFactory.getLogger(ControllerFormPropertyLoader.class);


    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private static final String RESOURCE_PATTERN = "/**/*.class";

    private List<String> basePackage;

    private String parentClassPath;


    public void load() {
        LOGGER.info("开始预加载Controller Form的属性到缓存*********************");
        this.resolve();
        LOGGER.info("结束预加载Controller Form的属性到缓存*********************");
    }

    public ControllerFormPropertyLoader(List<String> basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 进行解析操作
     *
     * @return
     */
    private void resolve() {
        int i = 0;
        try {
            Class parentClazz;
            if (StringUtils.isEmpty(parentClassPath)) {
                parentClazz = BaseForm.class;
                parentClassPath = parentClazz.getName();
            } else {
                parentClazz = Class.forName(parentClassPath);
            }
            LOGGER.info("查找：" + parentClazz.getName() + " 子类");
            if (basePackage != null) {
                for (String pkg : basePackage) {
                    String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
                    Resource[] resources = resourcePatternResolver.getResources(pattern);
                    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            MetadataReader reader = readerFactory.getMetadataReader(resource);
                            String className = reader.getClassMetadata().getClassName();
                            Class clazz = Class.forName(className);
                            if (parentClazz.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug(className);
                                }
                                //执行这条语句，Spring会把相应的属性加载到缓存中
                                BeanUtils.getPropertyDescriptors(clazz);
                                i++;
                            }
                        }
                    }
                }
            } else {
                LOGGER.info("未进行配置：form的包");
            }
            LOGGER.info("总共查找：" + parentClassPath + " 子类" + i + "个");
        } catch (Exception ex) {
            LOGGER.error("解析Controller Form：" + parentClassPath + "子类失败");
            ex.printStackTrace();
        }
    }


    public void setParentClassPath(String parentClassPath) {
        this.parentClassPath = parentClassPath;
    }

}
