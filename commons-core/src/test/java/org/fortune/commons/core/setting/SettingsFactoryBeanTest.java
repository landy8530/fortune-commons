package org.fortune.commons.core.setting;

import org.fortune.commons.core.BaseJunit4Test;
import org.fortune.commons.core.setting.exception.ResolveFailedConfigException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@SpringApplicationConfiguration(classes = Application.class, initializers = YamlFileApplicationContextInitializer.class)
public class SettingsFactoryBeanTest extends BaseJunit4Test {

    @Autowired
    private Settings settings;

    @Test
    public void settingBeanLoadTest() {
        Assert.assertNotNull(settings);
        Assert.assertEquals("testValue",settings.get("module01.testStringKey"));
        Assert.assertTrue( 9 == settings.getAsInt("module02.testIntKey"));
    }

    @Test(expected = ResolveFailedConfigException.class)
    public void testExceptionWhenSetAWrongConfigPath(){
        SettingsFactoryBean settingsFactoryBean = new SettingsFactoryBean();
        settingsFactoryBean.setConfigFilePath("wrongPath");
        settingsFactoryBean.createInstance();
    }
}
