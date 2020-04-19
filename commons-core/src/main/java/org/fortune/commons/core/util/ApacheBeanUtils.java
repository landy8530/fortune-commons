package org.fortune.commons.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ObjectUtils;

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
                Date date = DateUtil.string2Date(ObjectUtils.nullSafeToString(bean));
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
