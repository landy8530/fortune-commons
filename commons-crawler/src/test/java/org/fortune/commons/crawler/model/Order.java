package org.fortune.commons.crawler.model;

import org.fortune.commons.crawler.annotation.PageFieldSelect;
import org.fortune.commons.crawler.annotation.PageSelect;

import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/23 09:48
 * @description:
 */
@PageSelect(cssQuery = "#_frm_order_list_query > div.common_tablediv.w > div.content > table")
public class Order {

    @PageFieldSelect(cssQuery = "td.orderlink")
    List<String> orderNumbers;

    public List<String> getOrderNumbers() {
        return orderNumbers;
    }
}
