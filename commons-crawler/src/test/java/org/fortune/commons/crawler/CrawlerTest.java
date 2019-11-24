package org.fortune.commons.crawler;

import org.fortune.commons.core.http.HttpClient;
import org.fortune.commons.core.http.HttpResponse;
import org.fortune.commons.crawler.model.Order;
import org.fortune.commons.crawler.model.OrderSummary;
import org.fortune.commons.crawler.parser.PageParser;
import org.fortune.commons.crawler.parser.PageParserProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author: landy
 * @date: 2019/11/21 22:51
 * @description:
 */
public class CrawlerTest {

    public static void main(String[] args) throws Exception {
        HttpClient httpClient = new HttpClient();
        BrowserCompatibleRedirectHandler redirectHandler = new BrowserCompatibleRedirectHandler();

        CrawlerClient crawlerClient = new CrawlerClient(httpClient, redirectHandler);

        crawlerClient.url("https://www.cndpp.com/client/index.jsp").submit();

        HttpResponse response = crawlerClient.post("https://www.cndpp.com/client/login")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6")
                .addHeader("Cache-Control","max-age=0")
                .addHeader("Connection","keep-alive")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Host","www.cndpp.com")
                .addHeader("Origin","https://www.cndpp.com")
                .addHeader("Referer","https://www.cndpp.com/client/index.jsp")
                .addHeader("Sec-Fetch-Mode","navigate")
                .addHeader("Sec-Fetch-Site","same-origin")
                .addHeader("Sec-Fetch-User","?1")
                .addHeader("Upgrade-Insecure-Requests","1")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
                .addParam("username","xxxx")
                .addParam("password","yyyy")
                .addParam("imgCode","")
                .submit();

        System.err.println(response);

        response = crawlerClient.url("https://www.cndpp.com/client/order/order-list.htm").submit();
        String responseText = response.getResponseText();
        Document document = Jsoup.parse(responseText);
        PageParserProcessor.processPage(document, new PageParser<Order>() {
            @Override
            public void parse(Document html, Element pageVoElement, Order pageVo) {
                String pageUrl = html.baseUri();
                System.out.println(pageUrl + "=====" + pageVo.getOrderNumbers());
            }
        });

        response = crawlerClient.url("https://www.cndpp.com/client/order/order-detail.htm?id=1493400901")
                .addGetHeader("Referer","https://www.cndpp.com/client/order/view-order-summary.htm?id=1493400901")
                .submit();
        String responseText1 = response.getResponseText();
        Document document1 = Jsoup.parse(responseText1);
        PageParserProcessor.processPage(document1, new PageParser<OrderSummary>() {
            @Override
            public void parse(Document html, Element pageVoElement, OrderSummary pageVo) {
                String pageUrl = html.baseUri();
                System.out.println(pageUrl + "=====" + pageVo.getOrderNo());
            }
        });
    }

}
