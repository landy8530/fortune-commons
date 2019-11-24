package org.fortune.commons.crawler.model;

import org.fortune.commons.crawler.annotation.PageFieldSelect;
import org.fortune.commons.crawler.annotation.PageSelect;

/**
 * @author: landy
 * @date: 2019/11/24 10:43
 * @description:
 */
@PageSelect(cssQuery = "#orderHeader")
public class OrderSummary {

    @PageFieldSelect(cssQuery = "tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2)")
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }
}
