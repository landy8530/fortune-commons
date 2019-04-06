package org.landy.commons.core.bean.copy;

import org.landy.commons.core.domain.Child;
import org.landy.commons.core.domain.Root;
import org.landy.commons.core.util.BeanCopierUtil;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BeanCopyPerformanceTest {
    /**
     * CGLib BeanCopier花费时间:5110ms
     * Spring BeanUtils花费时间:2842ms
     * Apache BeanUtils花费时间:34745ms
     * Apache PropertyUtils花费时间:26208ms
     */
//    private static final long COUNT = 10000000;
    /**
     * CGLib BeanCopier花费时间:1111ms
     * Spring BeanUtils花费时间:1462ms
     * Apache BeanUtils花费时间:4109ms
     * Apache PropertyUtils花费时间:2833ms
     * */
    private static final long COUNT = 1000000;
    static Child child = new Child();
    static Root root = new Root();
    static {
        child.setId(1L);
        child.setName("name1");
        List<Child> list = new ArrayList<>();
        list.add(child);

        child = new Child();
        child.setId(2L);
        child.setName("name2");
        list.add(child);
        root.setId(2L);
        root.setList(list);
        root.setIndex(100);
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        System.gc();
        long startTime = System.currentTimeMillis();
        for(int i=0;i<COUNT;i++) {
            Root copyRoot = new Root();
            BeanCopierUtil.copyProperties(root,copyRoot);
        }
        System.out.println("CGLib BeanCopier花费时间:" + (System.currentTimeMillis() - startTime) + "ms");
        System.gc();

        startTime = System.currentTimeMillis();
        for(int i=0;i<COUNT;i++) {
            Root copyRoot = new Root();
            BeanUtils.copyProperties(root,copyRoot);
        }
        System.out.println("Spring BeanUtils花费时间:" + (System.currentTimeMillis() - startTime) + "ms");
        System.gc();
        startTime = System.currentTimeMillis();
        for(int i=0;i<COUNT;i++) {
            Root copyRoot = new Root();
            org.apache.commons.beanutils.BeanUtils.copyProperties(root,copyRoot);
        }
        System.out.println("Apache BeanUtils花费时间:" + (System.currentTimeMillis() - startTime) + "ms");
        System.gc();
        startTime = System.currentTimeMillis();
        for(int i=0;i<COUNT;i++) {
            Root copyRoot = new Root();
            org.apache.commons.beanutils.PropertyUtils.copyProperties(root,copyRoot);
        }
        System.out.println("Apache PropertyUtils花费时间:" + (System.currentTimeMillis() - startTime) + "ms");


    }

}
