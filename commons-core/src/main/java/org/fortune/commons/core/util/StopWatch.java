package org.fortune.commons.core.util;

/**
 * @author: landy
 * @date: 2019/11/21 00:06
 * @description: 简单实现任务执行时间记录器
 */
public class StopWatch {

    private long start;

    public StopWatch() {
        this.reset();
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }

    public long elapsedTime() {
        long end = System.currentTimeMillis();
        return end - this.start;
    }

}
