package org.fortune.commons.export.excel.jxls.tag;

import net.sf.jxls.transformer.SheetTransformer;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 12:40
 * @description:
 */
public class MapTag extends AbstractBaseOutTag {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 932991928216241306L;

    /**
     * TAG_NAME
     */
    public static final String TAG_NAME = "map";
    /**
     * 选项列表数据，可以是list,iterator,array,map等类型，可以使用request中的名字或者通过${}获取的对象。从其他对象的属性得到的数据需要使用EL表达式获取
     */
    private Object items = null;

    /**
     * 选中值，支持EL表达式
     */
    private String value = null; //

    /**
     * 默认值，支持EL表达式
     * 用于当前值为空的情况
     */
    private String defaultValue = null; //
    /**
     * 用于从对象中获取值，作为option的value
     */
    private String valueField;//

    /**
     * 用于从对象中获取值，作为option的text
     */
    private String labelField;//


    // ////////////////////////////////////////////

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValueField() {
        return valueField;
    }

    public void setValueField(String valueField) {
        this.valueField = valueField;
    }

    public String getLabelField() {
        return labelField;
    }

    public void setLabelField(String labelField) {
        this.labelField = labelField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return TAG_NAME;
    }

    /**
     * @param sheetTransformer
     * @return
     * @throws Exception
     */
    protected Object processValue(SheetTransformer sheetTransformer) throws Exception {
        String result = null;

        Object selectValue;
        if (value != null && !"".equals(value)) {
            selectValue = getExprValue(value);
            //按默认值处理
        } else if (defaultValue != null && !defaultValue.equals("")) {
            selectValue = getExprValue(defaultValue);
        } else {
            selectValue = "";
        }

        if (items == null || items.equals("")) {
            return selectValue;
        }
        Object options = items;
        if (items instanceof String) {
            options = getExprValue((String) items);
        }
        if (options == null) {
            return selectValue;
        }


        Object selectItem = null;
        if (options instanceof Map) {
            Map map = (Map) options;
            selectItem = map.get(selectValue);
        } else if (options instanceof Iterator || options instanceof Collection) {
            Iterator it;
            if (options instanceof Iterator) {
                it = (Iterator) options;
            } else {
                it = ((Collection) options).iterator();
            }
            while (it.hasNext()) {
                Object entry = it.next();
                selectItem = this.calculateSelectedItem(entry, selectValue);
            }
        } else if (options.getClass().isArray()) {// 数组
            int length = Array.getLength(options);
            for (int j = 0; j < length; j++) {
                Object entry = Array.get(options, j);
                selectItem = this.calculateSelectedItem(entry, selectValue);
            }
        }
        if (selectItem != null) {
            result = selectItem.toString();
        }
        if (result == null) {
            result = selectValue != null ? selectValue.toString() : "";
        }
        return result;
    }

    private Object calculateSelectedItem(Object entry, Object selectValue) {
        Object text;
        Object key;
        Object selectItem = null;
        if (valueField == null || valueField.equals("")) {
            key = entry.toString();
        } else {
            key = getPropertyValue(valueField, entry);
        }
        if (labelField == null || labelField.equals("")) {
            text = key;
        } else {
            text = getPropertyValue(labelField, entry);
        }
        if (key.equals(selectValue)) {
            selectItem = text;
        }
        return selectItem;
    }
}
