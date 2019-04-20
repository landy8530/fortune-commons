package net.sf.jxls.tag;

import org.landy.commons.export.excel.jxls.tag.DateTimeTag;
import org.landy.commons.export.excel.jxls.tag.MapTag;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 12:48
 * @description: 扩展jxls的默认标签以支持自定标签，如代码翻译和时间格式化输出等
 */
public class JxTaglib implements TagLib {
    /**
     * tagName
     */
    static String[] tagName = new String[]{"forEach", "if", "outline", "out", "map", "dateTime"};

    /**
     * tagClassName
     */
    static String[] tagClassName = new String[]{"net.sf.jxls.tag.ForEachTag", "net.sf.jxls.tag.IfTag",
            "net.sf.jxls.tag.OutlineTag", "net.sf.jxls.tag.OutTag",
            MapTag.class.getName(), DateTimeTag.class.getName()};

    /**
     * tags
     */
    static Map tags = new HashMap();

    static {
        for (int i = 0; i < tagName.length; i++) {
            String key = tagName[i];
            String value = tagClassName[i];
            tags.put(key, value);
        }
    }

    @Override
    public Map getTags() {
        return tags;
    }
}
