package org.landy.commons.export.excel.jxls.tag;

import net.sf.jxls.transformer.SheetTransformer;
import org.landy.commons.core.utils.DateUtil;

/**
 * @author: Landy
 * @date: 2019/4/6 12:29
 * @description:
 */
public class DateTimeTag extends AbstractBaseOutTag {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 932991928216241308L;
    /**
     * TAG_NAME
     */
    public static final String TAG_NAME = "dateTime";
    /**
     * 选中值，支持EL表达式
     */
    private String value = null; //

    /**
     * format 日期时间格式 <br/>
     * 1 表示yyyymmdd 或yyyymmddHHMiSS <br/>
     * 2 用连字符-分隔的时间时间格式串，如yyyy-mm-dd 或yyyy-mm-dd HH:Mi:SS <br/>
     * 3 用连字符.分隔的时间时间格式串，如yyyy.mm.dd 或yyyy.mm.dd HH:Mi:SS <br/>
     * 4 用中文字符分隔的时间格式串，如yyyy年mm月dd 或yyyy年mm月dd HH:Mi:SS
     */
    private Integer format;

    /**
     * length 长度，用于截取格式化后的字符
     */
    private Integer length;

    /**
     * 处理输出值，进行日期格式化
     *
     * @param sheetTransformer sheetTransformer
     * @return 格式化后的日期
     * @throws Exception Exception
     */
    protected Object processValue(SheetTransformer sheetTransformer) throws Exception {
        String result = null;

        value = (String) getExprValue(value);
        if (value == null || value.equals("")) {
            return "";
        }
        if (format == null || format.intValue() > 4 || format.intValue() < 1) {
            format = new Integer(DateUtil.HYPHEN_DISPLAY_DATE);
        }
        result = DateUtil.toDisplayStr(value, format.intValue());
        if (length != null && length.intValue() < result.length() && length.intValue() > 0) {
            result = result.substring(0, length.intValue());
        }
        return result;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
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

}
