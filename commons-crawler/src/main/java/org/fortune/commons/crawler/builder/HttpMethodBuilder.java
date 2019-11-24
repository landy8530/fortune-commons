package org.fortune.commons.crawler.builder;

import org.fortune.commons.core.http.HttpBinaryResponse;
import org.fortune.commons.core.http.HttpResponse;


public interface HttpMethodBuilder {
    /**
     * 直接爬取Web内容
     * @param onceRedirect
     * @return
     */
    HttpResponse submit(boolean onceRedirect);

    /**
     * 直接爬取Web内容
     * @return
     */
    HttpResponse submit();

    HttpBinaryResponse download();

}
