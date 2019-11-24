package org.fortune.commons.crawler.annotation;

import java.lang.annotation.*;

/**
 * page vo annotation
 *
 * 页面数据对象 注解
 *
 * @author xuxueli 2017-10-17 20:28:11
 *
 * 参考自 https://github.com/xuxueli/xxl-crawler/
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PageSelect {

    /**
     * CSS-like query, like "#body"
     *
     * CSS选择器, 如 "#body"
     *
     * @return String
     */
    public String cssQuery() default "";

}
