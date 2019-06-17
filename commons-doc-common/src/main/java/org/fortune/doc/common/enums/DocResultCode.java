package org.fortune.doc.common.enums;

/**
 * @author: landy
 * @date: 2019/6/16 13:59
 * @description:
 */
public enum DocResultCode {

    SUCCESS("成功", 1),
    FAILED("失败", 0);

    private String name;
    private int value;

    private DocResultCode(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
