package org.landy.commons.web.domain;

import java.util.Set;

/**
 * @author: Landy
 * @date: 2019/5/7 22:49
 * @description:
 */
public class Menu {

    // fields
    private String menuName;
    private String url;
    private Long sortNo;

    // collections
    private Set<Menu> childMenus;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public Set<Menu> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(Set<Menu> childMenus) {
        this.childMenus = childMenus;
    }
}
