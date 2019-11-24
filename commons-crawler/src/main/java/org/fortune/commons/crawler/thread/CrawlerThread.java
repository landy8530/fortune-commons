package org.fortune.commons.crawler.thread;

import org.fortune.commons.core.http.HttpResponse;
import org.fortune.commons.core.util.URLUtils;
import org.fortune.commons.crawler.CrawlerClient;
import org.fortune.commons.crawler.CrawlerThreadClient;
import org.fortune.commons.crawler.builder.GetMethodBuilder;
import org.fortune.commons.crawler.builder.PostMethodBuilder;
import org.fortune.commons.crawler.parser.PageParserProcessor;
import org.fortune.commons.crawler.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: landy
 * @date: 2019/11/23 00:14
 * @description:
 */
public class CrawlerThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

    private CrawlerThreadClient crawlerThreadClient;
    private CrawlerClient crawlerClient;
    private boolean running;
    private boolean toStop;

    public CrawlerThread(CrawlerThreadClient crawlerThreadClient, CrawlerClient crawlerClient) {
        this.crawlerThreadClient = crawlerThreadClient;
        this.crawlerClient = crawlerClient;
        this.running = true;
        this.toStop = false;
    }

    public void toStop() {
        this.toStop = true;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        while (!toStop) {
            try {

                // ------- url ----------
                String link = crawlerThreadClient.getRunData().getUrl();
                running = true;
                logger.info(">>>>>>>>>>> crawler, process link : {}", link);
                if (!URLUtils.isUrl(link)) {
                    continue;
                }

                // failover
                for (int i = 0; i < (1 + crawlerThreadClient.getCrawlerConf().getFailRetryCount()); i++) {

                    boolean ret = false;
                    try {
                        boolean ifPost = crawlerThreadClient.getCrawlerConf().isIfPost();
                        Map<String,String> paramMap = crawlerThreadClient.getCrawlerConf().getParamMap();
                        Map<String,String> headerMap = crawlerThreadClient.getCrawlerConf().getHeaderMap();
                        HttpResponse response;
                        if(ifPost) {
                            PostMethodBuilder postMethodBuilder = crawlerClient.post(link);
                            if(headerMap != null) {
                                for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                                    postMethodBuilder.addHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            if(paramMap != null) {
                                for(Map.Entry<String, String> entry : paramMap.entrySet()) {
                                    postMethodBuilder.addParam(entry.getKey(), entry.getValue());
                                }
                            }
                            response = postMethodBuilder.submit();
                        } else {
                            GetMethodBuilder getMethodBuilder = crawlerClient.url(link);
                            if(headerMap != null) {
                                for(Map.Entry<String, String> entry : headerMap.entrySet()) {
                                    getMethodBuilder.addGetHeader(entry.getKey(), entry.getValue());
                                }
                            }
                            response = getMethodBuilder.submit();
                        }

                        String html = response.getResponseText();

                        ret = processPage(html, link);
                    } catch (Throwable e) {
                        logger.info(">>>>>>>>>>> crawler process error.", e);
                    }

                    if (crawlerThreadClient.getCrawlerConf().getPauseMillis() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(crawlerThreadClient.getCrawlerConf().getPauseMillis());
                        } catch (InterruptedException e) {
                            logger.info(">>>>>>>>>>> crawler thread is interrupted. {}", e.getMessage());
                        }
                    }
                    if (ret) {
                        break;
                    }
                }

            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    logger.info(">>>>>>>>>>> crawler thread is interrupted. {}", e.getMessage());
                } else {
                    logger.error(e.getMessage(), e);
                }
            } finally {
                running = false;
                crawlerThreadClient.tryFinish();
                running = true;
            }

        }
    }

    private boolean processPage(String htmlText, String link) throws IllegalAccessException, InstantiationException {
        Document html = Jsoup.parse(htmlText);
        // ------- child link list (FIFO队列,广度优先) ----------
        if (crawlerThreadClient.getCrawlerConf().isAllowSpread()) {     // limit child spread
            Set<String> links = JsoupUtil.findLinks(html);
            if (links != null && links.size() > 0) {
                for (String item : links) {
                    if (crawlerThreadClient.getCrawlerConf().validWhiteUrl(item)) {      // limit unvalid-child spread
                        crawlerThreadClient.getRunData().addUrl(item);
                    }
                }
            }
        }

        // ------- pagevo ----------
        if (!crawlerThreadClient.getCrawlerConf().validWhiteUrl(link)) {     // limit unvalid-page parse, only allow spread child, finish here
            return true;
        }
        return PageParserProcessor.processPage(html, crawlerThreadClient.getCrawlerConf().getPageParser());
    }
}
