package org.landy.common.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.core.conf.SettingsConfiguration;
import org.landy.commons.datacache.DataCacheFacade;
import org.landy.commons.datacache.conf.DataCacheConfiguration;
import org.landy.commons.web.conf.WebApplicationContextConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        WebApplicationContextConfiguration.class
})
public class WebApplicationContextTest {

    @Test
    public void test() {
        System.out.println("hello,world!");
    }

}
