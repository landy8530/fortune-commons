package org.landy.commons.core.utils;

import org.landy.commons.core.cglib.BeanCopierContainer;
import org.springframework.cglib.beans.BeanCopier;

public class BeanCopyUtil {

    /**
     * https://blog.csdn.net/jianhua0902/article/details/8155368
     * https://www.programcreek.com/java-api-examples/index.php?api=net.sf.cglib.beans.BeanCopier
     * @param from
     * @param to
     * @return
     */
    public static void copyProperties(Object from, Object to) {
        BeanCopier beanCopier = BeanCopierContainer.accessBeanCopier(to.getClass());
        beanCopier.copy(from, to, null);
    }

}
