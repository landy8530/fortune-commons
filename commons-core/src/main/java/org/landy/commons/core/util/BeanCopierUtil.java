package org.landy.commons.core.util;

import org.landy.commons.core.cglib.BeanCopierContainer;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* https://blog.csdn.net/jianhua0902/article/details/8155368
* https://www.programcreek.com/java-api-examples/index.php?api=net.sf.cglib.beans.BeanCopier
* @author: Landy
* @date:   2019/4/4 23:21
* @version: 1.0
*/
public class BeanCopierUtil {
    /**
     * 存放BeanCopier对象
     */
    private static final Map<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();
    /**
     * 此方法只能Copy相同Class类型的对象,通过BeanCopierContainer预加载了所有Bean对应的BeanCopier对象
     * @param source 源对象
     * @param target 目标对象
     * @return
     * @see BeanCopierContainer
     */
    public static <T> void copyUniformProperties(T source, T target) {
        BeanCopier beanCopier = BeanCopierContainer.accessBeanCopier(target.getClass());
        beanCopier.copy(source, target, null);
    }

    /**
     * 此方法可以copy不同对象相同类型，相同属性名的字段。
     * Create对象过程：产生sourceClass-》TargetClass的拷贝代理类，放入jvm中，所以创建的代理类的时候比较耗时。
     * 最好保证这个对象的单例模式，此处使用Map缓存BeanCopier对象，不需要每次重新创建。
     * @author Landy
     * @param source 源对象
     * @param target 目标对象
     * @return
     * @throws
     * @date  2019/4/4 23:07
    */
    public static <S,T> void copyProperties(S source, T target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }
}
