package org.fortune.commons.crawler.builder;

import org.fortune.commons.crawler.parser.PageParser;

import java.util.Map;

/**
 * @author: landy
 * @date: 2019/11/23 00:17
 * @description: 利用多线程方式实现爬虫配置器Builder
 */
public interface CrawlerThreadConfigurationBuilder {

    CrawlerThreadConfigurationBuilder setHeaderMap(Map<String, String> headerMap);

    CrawlerThreadConfigurationBuilder setParamMap(Map<String, String> paramMap);
    /**
     * 允许扩散爬取，将会以现有URL为起点扩散爬取整站
     *
     * @param allowSpread
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setAllowSpread(boolean allowSpread) ;

    /**
     * URL白名单正则，非空时进行URL白名单过滤页面
     *
     * @param whiteUrlRegexs
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setWhiteUrlRegexs(String... whiteUrlRegexs);

    /**
     * 页面解析器
     *
     * @param pageParser
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setPageParser(PageParser pageParser);

    /**
     * 超时时间，毫秒
     *
     * @param timeoutMillis
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setTimeoutMillis(int timeoutMillis) ;

    /**
     * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
     *
     * @param pauseMillis
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setPauseMillis(int pauseMillis) ;

    /**
     * 失败重试次数，大于零时生效
     *
     * @param failRetryCount
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setFailRetryCount(int failRetryCount) ;

    // thread
    /**
     * 爬虫并发线程数
     *
     * @param threadCount
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setThreadCount(int threadCount) ;

    /**
     * 待爬的URL列表
     *
     * @param urls
     * @return Builder
     */
    CrawlerThreadConfigurationBuilder setUrls(String... urls);

    CrawlerThreadConfigurationBuilder setIfPost(boolean ifPost);

    void submit(boolean sync);

}
