package org.fortune.commons.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: landy
 * @date: 2019/11/27 23:39
 * @description:
 */
public final class ApacheBeanUtils {
    static {
        ConvertUtils.register(new DateConverter(), Date.class);
    }

    public static void copyProperties(Object dest, Object origin) {
        try {
            BeanUtils.copyProperties(dest, origin);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static class DateConverter implements Converter {
        @Override
        public <T> T convert(Class<T> type, Object value) {
            SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String newValue = value.toString();
                if(newValue.length() == 10) {
                    return (T)simpleDateFormat.parse(newValue);
                } else {
                    return (T)simpleDateTimeFormat.parse(newValue);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
