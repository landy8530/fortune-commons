package org.landy.commons.core.cglib;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.commons.core.conf.BeanCopierConfiguration;
import org.landy.commons.core.domain.Child;
import org.landy.commons.core.domain.Root;
import org.landy.commons.core.setting.conf.SettingsConfiguration;
import org.landy.commons.core.utils.BeanCopyUtil;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SettingsConfiguration.class,
        BeanCopierConfiguration.class
})
public class BeanCopyUtilTest {

    @Test
    public void test() {

        Child child = new Child();
        child.setId(1L);
        child.setName("name1");
        child.setDate(new Date());
        List<Child> list = new ArrayList<>();
        list.add(child);

        child = new Child();
        child.setId(2L);
        child.setName("name2");
        child.setDate(new Date());
        list.add(child);

        Root root = new Root();
        root.setId(2L);
        root.setDate(new java.sql.Date(System.currentTimeMillis()));
        root.setList(list);

        Root copyRoot = new Root();

        BeanCopyUtil.copyProperties(root,copyRoot);

        System.out.println(copyRoot.getList().size());
    }


}

