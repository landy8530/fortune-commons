package org.fortune.commons.crawler.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *  页面内容解析器，需要实现端自己实现具体的业务逻辑
 *  参考自 https://github.com/xuxueli/xxl-crawler/
 */
public abstract class PageParser<T> {

    /**
     * parse pageVo
     *
     * @param html              page html data
     * @param pageVoElement     pageVo html data
     * @param pageVo            pageVo object
     */
    public abstract void parse(Document html, Element pageVoElement, T pageVo);

}
