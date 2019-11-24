package org.fortune.commons.crawler;

import org.fortune.commons.crawler.builder.CrawlerThreadConfigurationBuilder;
import org.fortune.commons.crawler.model.CrawlerConfiguration;
import org.fortune.commons.crawler.parser.PageParser;
import org.fortune.commons.crawler.thread.CrawlerThread;
import org.fortune.commons.crawler.thread.request.LocalURLPool;
import org.fortune.commons.crawler.thread.request.URLPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: landy
 * @date: 2019/11/24 12:38
 * @description:
 */
public class CrawlerThreadClient implements CrawlerThreadConfigurationBuilder {
    protected Logger LOGGER = LoggerFactory.getLogger(CrawlerThreadClient.class);

    private CrawlerClient crawlerClient;

    // crawler conf
    private volatile CrawlerConfiguration crawlerConf = new CrawlerConfiguration();                               // 运行时配置

    // run data
    private volatile URLPool runData = new LocalURLPool();                          // 运行时数据模型 ,目前只有配置URL列表池

    // thread
    private int threadCount = 1;                                                    // 爬虫线程数量
    private ExecutorService crawlers = Executors.newCachedThreadPool();             // 爬虫线程池
    private List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();     // 爬虫线程引用镜像

    public CrawlerThreadClient(CrawlerClient crawlerClient) {
        this.crawlerClient = crawlerClient;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setAllowSpread(boolean allowSpread) {
        this.crawlerConf.setAllowSpread(allowSpread);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setWhiteUrlRegexs(String... whiteUrlRegexs) {
        if (whiteUrlRegexs!=null && whiteUrlRegexs.length>0) {
            for (String whiteUrlRegex: whiteUrlRegexs) {
                this.crawlerConf.getWhiteUrlRegexes().add(whiteUrlRegex);
            }
        }
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setPageParser(PageParser pageParser) {
        this.crawlerConf.setPageParser(pageParser);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setTimeoutMillis(int timeoutMillis) {
        this.crawlerConf.setTimeoutMillis(timeoutMillis);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setPauseMillis(int pauseMillis) {
        this.crawlerConf.setPauseMillis(pauseMillis);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setFailRetryCount(int failRetryCount) {
        this.crawlerConf.setFailRetryCount(failRetryCount);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setUrls(String... urls) {
        if (urls!=null && urls.length>0) {
            for (String url: urls) {
                this.runData.addUrl(url);
            }
        }
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setIfPost(boolean ifPost) {
        this.crawlerConf.setIfPost(ifPost);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setHeaderMap(Map<String, String> headerMap) {
        this.crawlerConf.setHeaderMap(headerMap);
        return this;
    }

    @Override
    public CrawlerThreadConfigurationBuilder setParamMap(Map<String, String> paramMap) {
        this.crawlerConf.setParamMap(paramMap);
        return this;
    }

    public CrawlerConfiguration getCrawlerConf() {
        return crawlerConf;
    }

    public URLPool getRunData() {
        return runData;
    }

    // ---------------------- crawler thread ----------------------

    /**
     * 启动
     *
     * @param sync  true=同步方式、false=异步方式
     */
    public void submit(boolean sync){
        if (this.runData == null) {
            throw new RuntimeException("crawler runData can not be null.");
        }
        if (this.runData.getUrlNum() <= 0) {
            throw new RuntimeException("crawler indexUrl can not be empty.");
        }
        if (crawlerConf == null) {
            throw new RuntimeException("crawler configuration can not be empty.");
        }
        if (threadCount<1 || threadCount>1000) {
            throw new RuntimeException("crawler threadCount invalid, threadCount : " + threadCount);
        }

        if (crawlerConf.getPageParser() == null) {
            throw new RuntimeException("crawler pageParser can not be null.");
        }

        LOGGER.info(">>>>>>>>>>> crawler start ...");
        for (int i = 0; i < threadCount; i++) {
            CrawlerThread crawlerThread = new CrawlerThread(this, crawlerClient);
            crawlerThreads.add(crawlerThread);
        }
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlers.execute(crawlerThread);
        }
        crawlers.shutdown();

        if (sync) {
            try {
                while (!crawlers.awaitTermination(5, TimeUnit.SECONDS)) {
                    LOGGER.info(">>>>>>>>>>> crawler still running ...");
                }
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 尝试终止
     */
    public void tryFinish(){
        boolean isRunning = false;
        for (CrawlerThread crawlerThread: crawlerThreads) {
            if (crawlerThread.isRunning()) {
                isRunning = true;
                break;
            }
        }
        boolean isEnd = !isRunning && this.runData.getUrlNum()==0 ;
        if (isEnd) {
            LOGGER.info(">>>>>>>>>>> crawler is finished.");
            stop();
        }
    }

    /**
     * 终止
     */
    public void stop(){
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlerThread.toStop();
        }
        crawlers.shutdownNow();
        LOGGER.info(">>>>>>>>>>> crawler stop.");
    }
}
