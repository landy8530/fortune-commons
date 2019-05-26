package org.fortune.commons.core.bean.copy;


import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ErrorBeanCopierTest {

    public static void main(String args[]) {
        SourceClass from = new SourceClass();
        from.setNum(1L);
        from.setName("name");
        UnSatisfiedBeanCopierObject to = new UnSatisfiedBeanCopierObject();
        System.out.println(ToStringBuilder.reflectionToString(from));
        //没有set方法的属性不拷贝
        BeanCopier copier = BeanCopier.create(SourceClass.class, UnSatisfiedBeanCopierObject.class, false);
        copier.copy(from,to,null);
        System.out.println("net.sf.cglib.beans.BeanCopier:" + ToStringBuilder.reflectionToString(to));
    }
}
