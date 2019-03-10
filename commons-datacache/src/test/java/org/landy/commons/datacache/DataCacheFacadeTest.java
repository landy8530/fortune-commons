package org.landy.commons.datacache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.core.setting.conf.SettingsConfiguration;
import org.landy.commons.datacache.conf.ApplicationContextConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SettingsConfiguration.class,
        ApplicationContextConfiguration.class,
        DataCacheConfig.class
})
public class DataCacheFacadeTest {

    @Test
    public void test() {
        System.out.println(DataCacheFacade.getInstance().getCacheKeyPrefix());
        System.out.println("======================" + DataCacheFacade.fetchService().fetchOfString("landy1"));
        System.out.println("======================" + DataCacheFacade.fetchService().fetchOfString("landy2"));
    }


}
