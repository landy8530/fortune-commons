package org.fortune.commons.core.util;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: landy
 * @date: 2020/3/26 21:48
 * @description:
 */
public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public ReflectionUtil() {
    }

    public static Object getFieldValue(Object target, String fieldName) {
        return getFieldValue(target, fieldName, true);
    }

    public static Object getFieldValue(Object target, String fieldName, boolean forceAccess) {
        try {
            return FieldUtils.readField(target, fieldName, forceAccess);
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static void setFieldValue(Object target, String fieldName, Object value) {
        setFieldValue(target, fieldName, value, true);
    }

    public static void setFieldValue(Object target, String fieldName, Object value, boolean forceAccess) {
        try {
            FieldUtils.writeField(target, fieldName, value, forceAccess);
        } catch (IllegalAccessException ex) {
            LOGGER.error("Occurred an unexpected exception",ex);
        }

    }

}
