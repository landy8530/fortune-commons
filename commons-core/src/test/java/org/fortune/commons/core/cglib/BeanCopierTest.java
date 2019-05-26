package org.fortune.commons.core.cglib;


import org.fortune.commons.core.domain.Child;
import org.fortune.commons.core.domain.Root;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

public class BeanCopierTest {

    public static void main(String[] args) {
        Child child = new Child();
        child.setId(1L);
        child.setName("name1");
        List<Child> list = new ArrayList<>();
        list.add(child);

        child = new Child();
//        child.setId(2L);
        child.setName("name2");
        list.add(child);

        Root root = new Root();
        root.setId(2L);
        root.setList(list);

        Root copyRoot = new Root();

        BeanCopier copier1 = BeanCopier.create(Root.class, Root.class, false);
        copier1.copy(root, copyRoot, null);

        System.out.println(copyRoot.getList());
        System.out.println(copyRoot.getId());
        //can beancopier in cglib deep copy this root to copyRoot?
        //because i found when i used beancopier the two instances still point the same list instance
    }

}
