package org.fortune.commons.datacache;

import org.fortune.commons.datacache.conf.DataCacheConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.fortune.commons.core.conf.SettingsConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SettingsConfiguration.class,
        DataCacheConfiguration.class,
        DataCacheFacadeConfig.class
})
public class DataCacheFacadeTest {

    @Test
    public void test() {
        System.out.println(DataCacheFacade.getInstance().getCacheKeyPrefix());
        System.out.println("======================" + DataCacheFacade.fetchService().fetchOfString("landy1"));
        System.out.println("======================" + DataCacheFacade.fetchService().fetchOfString("landy2"));
    }

}
