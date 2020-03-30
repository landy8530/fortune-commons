package org.fortune.commons.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private static Set<String> patterns_datetime = new HashSet();
    private static Set<String> patterns_date = new HashSet();

    static {
        patterns_date.add(DateUtil.PATTERN_FULL_DATE_SLASH);
        patterns_date.add(DateUtil.PATTERN_SHORT_DATE_SLASH);
        patterns_date.add(DateUtil.PATTERN_FULL_DATE_DASH);
        patterns_date.add(DateUtil.PATTERN_PERIOD_DATE);
        patterns_date.add(DateUtil.PATTERN_MONTH_YEAR);
        patterns_datetime.add(DateUtil.PATTERN_FULL_DATE_TIME_24);
        patterns_datetime.add(DateUtil.PATTERN_FULL_DATE_TIME_UNDERSCORE);
    }

    public static Object getProperty(Object bean, String name) {
        try {
            return PropertyUtils.getProperty(bean, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStringProperty(Object bean, String name) {
        try {
            return BeanUtils.getProperty(bean, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setProperty(Object bean, String name, Object value) {
        try {
            Class propertyType = PropertyUtils.getPropertyType(bean, name);
            if (propertyType != null && (propertyType.equals(Date.class) || propertyType.equals(java.sql.Date.class))) {
                Date date = convert(value);
                if(date != null) {
                    BeanUtils.setProperty(bean, name, date.getTime());
                } else {
                    BeanUtils.setProperty(bean, name, value);
                }
            } else {
                BeanUtils.setProperty(bean, name, value);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getPropertyType(Object bean, String name) {
        try {
            return PropertyUtils.getPropertyType(bean, name);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Date convert(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            Date dateObj = null;
            String dateStr = ObjectUtils.toString(value);
            Iterator it = patterns_date.iterator();
            if(dateStr.length() > 10) {
                it = patterns_datetime.iterator();
            }
            while (it.hasNext()) {
                try {
                    String pattern = (String) it.next();
                    dateObj = string2Date((String)value,pattern,false);
                    break;
                } catch (ParseException ex) {
                    //do iterator continue
                }
            }

            return dateObj;
        } else {
            return null;
        }
    }

    private static Date string2Date(String originalValue, String format, boolean lenient) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(lenient);
        return dateFormat.parse(originalValue);
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
