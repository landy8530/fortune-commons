package org.fortune.doc.common.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author: landy
 * @date: 2019/5/30 23:10
 * @description:
 */
public class JsonUtil {

    /**
     * 把对象转化为json字符串
     * @param obj
     * @return
     * @author:landyChris
     */
    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }
    /**
     * 把json字符串转化为相应的实体对象
     * @param json
     * @param clazz
     * @return
     * @author:landyChris
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

}
