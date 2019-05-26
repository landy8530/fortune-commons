package org.fortune.commons.core.bean.copy;

import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.InvocationTargetException;

public class BeanUtils4TypeTest {
    public static void main(String args[]) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
        SourceClass from = new SourceClass();
        from.setNum(1L);
        from.setName("name");
        TargetClass to = new TargetClass();
        System.out.println(ToStringBuilder.reflectionToString(from));

        org.apache.commons.beanutils.BeanUtils.copyProperties(to, from);//并能进行初级转换，Long和Integer互转
        System.out.println("org.apache.commons.beanutils.BeanUtils:" + ToStringBuilder.reflectionToString(to));
        //抛出参数不匹配异常 argument type mismatch - had objects of type "java.lang.Long" but expected signature "java.lang.Integer"
//        org.apache.commons.beanutils.PropertyUtils.copyProperties(to, from);
//        System.out.println("org.apache.commons.beanutils.PropertyUtils:" + ToStringBuilder.reflectionToString(to));
        org.springframework.beans.BeanUtils.copyProperties(from, to);//不会抛出异常，但是只拷贝部分属性
        System.out.println("org.springframework.beans.BeanUtils:" + ToStringBuilder.reflectionToString(to));
        //类型不匹配的，不拷贝属性值
        BeanCopier copier = BeanCopier.create(SourceClass.class, TargetClass.class, false);
        copier.copy(from,to,null);
        System.out.println("net.sf.cglib.beans.BeanCopier:" + ToStringBuilder.reflectionToString(to));
    }
}
