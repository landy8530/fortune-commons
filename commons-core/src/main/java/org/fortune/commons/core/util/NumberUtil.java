package org.fortune.commons.core.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author: landy
 * @date: 2020/3/26 00:25
 * @description:
 */
public class NumberUtil {

    public static final String PATTERN_INTEGER = "-?[0-9]+";
    public static final String PATTERN_DECIMAL = "-?[0-9]+\\.[0-9]+";
    public static final String PATTERN_NUMBER = ",###";
    public static final String PATTERN_CURRENCY = "$,##0.00";
    public static final String PATTERN_PERCENTAGE = "###%";
    public static final String PATTERN_DECIMAL_TWO = "#.##";

    public NumberUtil() {
    }

    public static int toInt(String str) {
        return NumberUtils.toInt(str);
    }

    public static double toDouble(String str) {
        return NumberUtils.toDouble(str);
    }

    public static BigDecimal toBigDecimal(String str) {
        return NumberUtils.createBigDecimal(str);
    }

    public static boolean isInteger(String str) {
        return StringUtils.isEmpty(str) ? false : str.matches("-?[0-9]+");
    }

    public static boolean isDecimal(String str) {
        return StringUtils.isEmpty(str) ? false : str.matches("-?[0-9]+\\.[0-9]+");
    }

    public static boolean isNumber(String str) {
        return isInteger(str) || isDecimal(str);
    }

    public static int getDecimalScale(String str) {
        int scale = 0;
        if (str.indexOf(".") > -1) {
            scale = str.length() - (str.indexOf(".") + 1);
        }

        return scale;
    }

    public static String getNumber(Object obj) {
        return format(obj, ",###");
    }

    public static String getCurrency(Object obj) {
        return format(obj, "$,##0.00");
    }

    public static String format(Object obj, String format) {
        NumberFormat formatter = new DecimalFormat(format);
        return formatter.format(obj);
    }

    public static String getPercentage(Object obj) {
        return format(obj, "###%");
    }

    public static boolean isEqual(BigDecimal firstValue, BigDecimal secondValue) {
        if (firstValue == null && secondValue == null) {
            return true;
        } else if (firstValue != null && secondValue == null) {
            return false;
        } else {
            return firstValue == null ? false : firstValue.equals(secondValue);
        }
    }

    public static double getPercent(double low, double high, double begin, double end) {
        double diff = 0.0D;
        double span = high - low;
        if (begin <= low && high <= end) {
            diff = span;
        }

        if (begin <= low && low <= end && end < high) {
            diff = end - low;
        }

        if (low < begin && end < high) {
            diff = end - begin + 0.01D;
        }

        if (low < begin && begin < high && high <= end) {
            diff = high - begin + 0.01D;
        }

        return diff / span;
    }

    public static int string2Int(String str) {
        return str == null ? 0 : Integer.parseInt(str);
    }

}
