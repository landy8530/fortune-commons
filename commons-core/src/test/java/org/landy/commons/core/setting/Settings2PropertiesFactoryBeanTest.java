package org.landy.commons.core.setting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.core.conf.SettingsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SettingsConfiguration.class})
public class Settings2PropertiesFactoryBeanTest {

    @Autowired
    private Settings settings;

    @Value("${module01.testStringKey}")
    private String stringValue;

    @Value("${module02.testIntKey}")
    private int intValue;

    @Test
    public void testGetObject() {
        Settings2PropertiesFactoryBean propertiesFactoryBean = new Settings2PropertiesFactoryBean();
        propertiesFactoryBean.setSettings(settings);
        Properties properties = propertiesFactoryBean.getObject();
        Assert.assertNotNull(properties);
        Assert.assertEquals("testValue",properties.get("module01.testStringKey"));
        Assert.assertTrue(propertiesFactoryBean.isSingleton());
        Class<Properties> clazz = propertiesFactoryBean.getObjectType();
        Assert.assertNotNull(clazz);

        System.out.println("stringValue==" + stringValue);
        Assert.assertEquals("testValue", stringValue);

        System.out.println("intValue==" + intValue);
        Assert.assertTrue( 9 == intValue);
    }
}
