package org.fortune.commons.core.util;

import java.util.concurrent.TimeUnit;

/**
 * @author: landy
 * @date: 2019/11/20 23:09
 * @description:
 */
public class TimeLength {
    public static final TimeLength ZERO = seconds(0L);
    private final long length;
    private final TimeUnit unit;

    public static TimeLength days(long days) {
        return new TimeLength(days, TimeUnit.DAYS);
    }

    public static TimeLength hours(long hours) {
        return new TimeLength(hours, TimeUnit.HOURS);
    }

    public static TimeLength minutes(long minutes) {
        return new TimeLength(minutes, TimeUnit.MINUTES);
    }

    public static TimeLength seconds(long seconds) {
        return new TimeLength(seconds, TimeUnit.SECONDS);
    }

    public static TimeLength milliseconds(long milliseconds) {
        return new TimeLength(milliseconds, TimeUnit.MILLISECONDS);
    }

    private TimeLength(long length, TimeUnit unit) {
        this.length = length;
        this.unit = unit;
    }

    public long length() {
        return this.length;
    }

    public TimeUnit unit() {
        return this.unit;
    }

    public long toMilliseconds() {
        return this.unit.toMillis(this.length);
    }

    public long toSeconds() {
        return this.unit.toSeconds(this.length);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof TimeLength)) {
            return false;
        } else {
            TimeLength that = (TimeLength)other;
            return this.toMilliseconds() == that.toMilliseconds();
        }
    }

    public int hashCode() {
        long mills = this.toMilliseconds();
        return (int)(mills ^ mills >>> 32);
    }
    
}
