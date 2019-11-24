package org.fortune.commons.crawler.thread.request;

import org.fortune.commons.core.exception.FortuneCrawlerException;
import org.fortune.commons.core.util.URLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: landy
 * @date: 2019/11/24 14:08
 * @description:
 */
public class LocalURLPool extends URLPool {

    private static Logger logger = LoggerFactory.getLogger(LocalURLPool.class);

    // url
    private volatile LinkedBlockingQueue<String> unVisitedUrlQueue = new LinkedBlockingQueue<String>();     // 待采集URL池
    private volatile Set<String> visitedUrlSet = Collections.synchronizedSet(new HashSet<String>());        // 已采集URL池


    /**
     * url add
     * @param link
     */
    @Override
    public boolean addUrl(String link) {
        if (!URLUtils.isUrl(link)) {
            logger.debug(">>>>>>>>>>> fortune-crawler addUrl fail, link not valid: {}", link);
            return false; // check URL格式
        }
        if (visitedUrlSet.contains(link)) {
            logger.debug(">>>>>>>>>>> fortune-crawler addUrl fail, link repeat: {}", link);
            return false; // check 未访问过
        }
        if (unVisitedUrlQueue.contains(link)) {
            logger.debug(">>>>>>>>>>> fortune-crawler addUrl fail, link visited: {}", link);
            return false; // check 未记录过
        }
        unVisitedUrlQueue.add(link);
        logger.info(">>>>>>>>>>> fortune-crawler addUrl success, link: {}", link);
        return true;
    }

    /**
     * url take
     * @return String
     * @throws InterruptedException
     */
    @Override
    public String getUrl() {
        String link ;
        try {
            link = unVisitedUrlQueue.take();
        } catch (InterruptedException e) {
            throw new FortuneCrawlerException("LocalURLPool.getUrl interrupted.");
        }
        if (link != null) {
            visitedUrlSet.add(link);
        }
        return link;
    }

    @Override
    public int getUrlNum() {
        return unVisitedUrlQueue.size();
    }


}
