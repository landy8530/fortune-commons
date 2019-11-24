package org.fortune.commons.crawler.thread.request;

/**
 * @author: landy
 * @date: 2019/11/24 14:07
 * @description: 线程方式启用 URL池的方式
 */
public abstract class URLPool {

    /**
     * add link
     *
     * @param link
     * @return boolean
     */
    public abstract boolean addUrl(String link);

    /**
     * get link, remove from unVisitedUrlQueue and add to visitedUrlSet
     *
     * @return String
     */
    public abstract String getUrl();

    /**
     * get url num
     *
     * @return int
     */
    public abstract int getUrlNum();

}
