package org.fortune.commons.web.springmvc.form;

import org.fortune.commons.core.page.PageInfo;

/**
 * @author: Landy
 * @date: 2019/4/20 15:42
 * @description:
 */
public class PageForm extends BaseForm {

    /**
     * 分页对象
     */
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        if(pageInfo==null){
            pageInfo=new PageInfo();
        }
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

}
