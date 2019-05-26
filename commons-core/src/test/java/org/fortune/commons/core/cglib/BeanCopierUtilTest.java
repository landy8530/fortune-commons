package org.fortune.commons.core.cglib;

import org.fortune.commons.core.conf.BeanCopierConfiguration;
import org.fortune.commons.core.conf.SettingsConfiguration;
import org.fortune.commons.core.domain.Child;
import org.fortune.commons.core.domain.Root;
import org.fortune.commons.core.domain.RootCopier;
import org.fortune.commons.core.util.BeanCopierUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class BeanCopierUtilTest {

    @Test
    public void copyUniformPropertiesTest() {

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

        BeanCopierUtil.copyUniformProperties(root,copyRoot);

        System.out.println(copyRoot.getList().size());
    }

    @Test
    public void copyPropertiesTest() {

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
        root.setIndex(100);
        RootCopier copyRoot = new RootCopier();

        BeanCopierUtil.copyProperties(root,copyRoot);

        System.out.println(copyRoot.getIndex());
    }

}

