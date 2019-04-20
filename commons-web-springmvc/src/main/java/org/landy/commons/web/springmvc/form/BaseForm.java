package org.landy.commons.web.springmvc.form;

import java.io.Serializable;

/**
 * @author: Landy
 * @date: 2019/4/20 15:41
 * @description:
 */
public class BaseForm implements Serializable {

    private String tuc;//时间戳字段

    public String getTuc() {
        return tuc;
    }

    public void setTuc(String tuc) {
        this.tuc = tuc;
    }

}
