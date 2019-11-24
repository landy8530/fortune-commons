package org.fortune.commons.crawler;

import org.fortune.commons.core.http.HttpClient;
import org.fortune.commons.crawler.model.Order;
import org.fortune.commons.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: landy
 * @date: 2019/11/24 11:58
 * @description: 多线程方式实现爬虫
 */
public class CrawlerThreadTest {

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();
        BrowserCompatibleRedirectHandler redirectHandler = new BrowserCompatibleRedirectHandler();

        CrawlerClient crawlerClient = new CrawlerClient(httpClient, redirectHandler);

        CrawlerThreadClient crawlerThreadClient = new CrawlerThreadClient(crawlerClient);

        //前置操作还需要登录操作
        Map<String, String> headMap = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();

        headMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headMap.put("Accept-Encoding","gzip, deflate, br");
        headMap.put("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6");
        headMap.put("Cache-Control","max-age=0");
        headMap.put("Connection","keep-alive");
        headMap.put("Content-Type","application/x-www-form-urlencoded");
        headMap.put("Host","www.cndpp.com");
        headMap.put("Origin","https://www.cndpp.com");
        headMap.put("Referer","https://www.cndpp.com/client/index.jsp");
        headMap.put("Sec-Fetch-Mode","navigate");
        headMap.put("Sec-Fetch-Site","same-origin");
        headMap.put("Sec-Fetch-User","?1");
        headMap.put("Upgrade-Insecure-Requests","1");
        headMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
        paramMap.put("username","xxx");
        paramMap.put("password","yyy");
        paramMap.put("imgCode","");

        crawlerThreadClient.setUrls("https://www.cndpp.com/client/login", "https://www.cndpp.com/client/order/order-list.htm")
                .setAllowSpread(false)
                .setThreadCount(1)
                .setHeaderMap(headMap)
                .setParamMap(paramMap)
                .setIfPost(true)
                .setPageParser(
                    new PageParser<Order>() {
                        @Override
                        public void parse(Document html, Element pageVoElement, Order pageVo) {
                            String pageUrl = html.baseUri();
                            System.out.println(pageUrl + "=====" + pageVo.getOrderNumbers());
                        }
                    })
                .submit(true);


    }

}
