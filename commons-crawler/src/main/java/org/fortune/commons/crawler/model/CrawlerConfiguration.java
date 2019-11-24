package org.fortune.commons.crawler.model;

import org.fortune.commons.core.util.URLUtils;
import org.fortune.commons.crawler.parser.PageParser;
import org.fortune.commons.crawler.util.RegexUtil;

import java.util.*;

/**
 * @author: landy
 * @date: 2019/11/23 00:01
 * @description: 爬虫页面解析配置类
 */
public class CrawlerConfiguration {
    // timeout default, ms
    public static final int TIMEOUT_MILLIS_DEFAULT = 5*1000;

    private volatile boolean allowSpread = true;                                    // 允许扩散爬取，将会以现有URL为起点扩散爬取整站
    private Set<String> whiteUrlRegexes = Collections.synchronizedSet(new HashSet<String>());    // URL白名单正则，非空时进行URL白名单过滤页面

    private PageParser pageParser;                                                  // 页面解析器

    private volatile int timeoutMillis = TIMEOUT_MILLIS_DEFAULT;                    // 超时时间，毫秒
    private volatile int pauseMillis = 0;                                           // 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；

    private volatile int failRetryCount = 0;                                        // 失败重试次数，大于零时生效

    private volatile boolean ifPost = false;                                        // 请求方式：true=POST请求、false=GET请求

    private volatile Map<String, String> paramMap;                                  // 请求参数
    private volatile Map<String, String> headerMap;                                 // 请求Header
    // util
    /**
     * valid url, include white url
     *
     * @param link
     * @return boolean
     */
    public boolean validWhiteUrl(String link){
        if (!URLUtils.isUrl(link)) {
            return false;   // false if url invalid
        }

        if (whiteUrlRegexes!=null && whiteUrlRegexes.size()>0) {
            boolean underWhiteUrl = false;
            for (String whiteRegex: whiteUrlRegexes) {
                if (RegexUtil.matches(whiteRegex, link)) {
                    underWhiteUrl = true;
                }
            }
            if (!underWhiteUrl) {
                return false;   // check white
            }
        }
        return true;    // true if regex is empty
    }

    // set/get

    public boolean isAllowSpread() {
        return allowSpread;
    }

    public void setAllowSpread(boolean allowSpread) {
        this.allowSpread = allowSpread;
    }

    public Set<String> getWhiteUrlRegexes() {
        return whiteUrlRegexes;
    }

    public void setWhiteUrlRegexes(Set<String> whiteUrlRegexes) {
        this.whiteUrlRegexes = whiteUrlRegexes;
    }

    public PageParser getPageParser() {
        return pageParser;
    }

    public void setPageParser(PageParser pageParser) {
        this.pageParser = pageParser;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public int getPauseMillis() {
        return pauseMillis;
    }

    public void setPauseMillis(int pauseMillis) {
        this.pauseMillis = pauseMillis;
    }

    public int getFailRetryCount() {
        return failRetryCount;
    }

    public void setFailRetryCount(int failRetryCount) {
        this.failRetryCount = failRetryCount;
    }

    public boolean isIfPost() {
        return ifPost;
    }

    public void setIfPost(boolean ifPost) {
        this.ifPost = ifPost;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }
}
