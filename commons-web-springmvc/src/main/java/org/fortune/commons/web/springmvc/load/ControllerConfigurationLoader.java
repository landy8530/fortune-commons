package org.fortune.commons.web.springmvc.load;

import org.fortune.commons.core.help.AbstractApplicationContextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Landy
 * @date: 2019/4/20 15:43
 * @description:
 */
public class ControllerConfigurationLoader extends AbstractApplicationContextHelper implements BeanFactoryAware {
    private Logger LOGGER = LoggerFactory.getLogger(ControllerConfigurationLoader.class);

    private DefaultListableBeanFactory beanFactory;

    private List<String> basePackage;

    private ControllerFormPropertyLoader formPropertyLoader;

    public ControllerConfigurationLoader(List<String> basePackage){
        this.basePackage=basePackage;
    }

    public void init() {

        formPropertyLoader = new ControllerFormPropertyLoader(this.basePackage);

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.setEnvironment(applicationContext.getEnvironment());
        Object bng = beanFactory.getSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR);
        ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        BeanNameGenerator beanNameGenerator;
        if (bng != null) {
            beanNameGenerator = new AnnotationBeanNameGenerator();
            beanFactory.registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
        } else {
            beanNameGenerator = (BeanNameGenerator) bng;
        }
        scanner.setBeanNameGenerator(beanNameGenerator);
        scanner.setScopeMetadataResolver(scopeMetadataResolver);
        formPropertyLoader.load();
        String[] bks = Arrays.copyOf(this.basePackage.toArray(), this.basePackage.size(), String[].class);
        LOGGER.info("开始扫描Controller路径*********************");
        scanner.scan(bks);
        LOGGER.info("结束扫描Controller路径*********************");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
