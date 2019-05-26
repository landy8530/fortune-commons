package org.fortune.commons.core.bean.copy;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class BeanUtilsTest {

    public static void main(String args[]) throws Throwable {
        BeanUtilObject from = new BeanUtilObject();
        BeanUtilObject to = new BeanUtilObject();
        from.setDate(new Date());
        from.setName("TTTT");
        System.out.println(ToStringBuilder.reflectionToString(from));
        //如果需要copy的bean定义在内部类中，则无法拷贝
        org.apache.commons.beanutils.BeanUtils.copyProperties(to, from);//如果from.setDate去掉，此处出现conveter异常
        System.out.println("org.apache.commons.beanutils.BeanUtils:" + ToStringBuilder.reflectionToString(to));
        org.apache.commons.beanutils.PropertyUtils.copyProperties(to, from);
        System.out.println("org.apache.commons.beanutils.PropertyUtils:" + ToStringBuilder.reflectionToString(to));
        org.springframework.beans.BeanUtils.copyProperties(from, to);
        System.out.println("org.springframework.beans.BeanUtils:" + ToStringBuilder.reflectionToString(to));
    }

}

