package org.landy.commons.core.setting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.core.conf.SettingsConfiguration;
import org.landy.commons.core.setting.exception.ResolveFailedConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SettingsConfiguration.class})
//@SpringApplicationConfiguration(classes = Application.class, initializers = YamlFileApplicationContextInitializer.class)
public class SettingsFactoryBeanTest {

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
