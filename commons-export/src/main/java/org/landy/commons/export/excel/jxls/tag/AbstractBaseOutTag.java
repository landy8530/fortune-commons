package org.landy.commons.export.excel.jxls.tag;

import net.sf.jxls.parser.Expression;
import net.sf.jxls.parser.ExpressionParser;
import net.sf.jxls.tag.BaseTag;
import net.sf.jxls.tag.Block;
import net.sf.jxls.tag.TagContext;
import net.sf.jxls.transformation.ResultTransformation;
import net.sf.jxls.transformer.Configuration;
import net.sf.jxls.transformer.Sheet;
import net.sf.jxls.transformer.SheetTransformer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 12:18
 * @description:
 */
public abstract class AbstractBaseOutTag extends BaseTag {
    private static final String POINT_NUMBER = ".";
    /**
     * LOGGER
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseOutTag.class);

    /**
     * TAG_NAME
     */
    public static final String TAG_NAME = "out";

    /**
     * configuration
     */
    protected Configuration configuration = new Configuration();

    /**
     * tagContext
     */
    protected TagContext tagContext;

    public String getName() {
        return TAG_NAME;
    }

    public TagContext getTagContext() {
        return tagContext;
    }

    /**
     * 初始化标签
     * @param context 标签上下文
     */
    public void init(TagContext context) {
        this.tagContext = context;
    }

    /**
     * 标签处理
     * @param sheetTransformer SheetTransformer
     * @return ResultTransformation
     */
    public ResultTransformation process(SheetTransformer sheetTransformer) {
        ResultTransformation resultTransformation = new ResultTransformation(0);
        try {
            //处理输出值，子类实现
            Object value = processValue(sheetTransformer);
            Block block = getTagContext().getTagBody();
            int rowNum = block.getStartRowNum();
            int cellNum = block.getStartCellNum();

            Sheet jxlsSheet = getTagContext().getSheet();
            if (jxlsSheet != null) {
                org.apache.poi.ss.usermodel.Sheet sheet = jxlsSheet.getPoiSheet();
                if (sheet != null) {
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        Cell cell = row.getCell((short) cellNum);
                        if (cell != null) {
                            // Object value = new Expression(expr,
                            // tagContext.getBeans(), configuration).evaluate();
                            if (value == null) {
                                cell.setCellValue(sheet.getWorkbook().getCreationHelper().createRichTextString(""));
                            } else if (value instanceof Double) {
                                cell.setCellValue(((Double) value).doubleValue());
                            } else if (value instanceof BigDecimal) {
                                cell.setCellValue(((BigDecimal) value).doubleValue());
                            } else if (value instanceof Date) {
                                cell.setCellValue((Date) value);
                            } else if (value instanceof Calendar) {
                                cell.setCellValue((Calendar) value);
                            } else if (value instanceof Integer) {
                                cell.setCellValue(((Integer) value).intValue());
                            } else if (value instanceof Long) {
                                cell.setCellValue(((Long) value).longValue());
                            } else {
                                // fixing possible CR/LF problem
                                String fixedValue = value.toString();
                                if (fixedValue != null) {
                                    fixedValue = fixedValue.replaceAll("\r\n", "\n");
                                }
                                cell.setCellValue(sheet.getWorkbook().getCreationHelper().createRichTextString(fixedValue));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Occurs an unexpected exception",e);
        }
        return resultTransformation;
    }

    /**
     * processValue 输出值处理，由子类实现
     *
     * @param sheetTransformer sheetTransformer
     * @return 输出值
     * @throws Exception 异常
     */
    protected abstract Object processValue(SheetTransformer sheetTransformer) throws Exception;

    /**
     * 获取对象属性值，可以支持object,也可以支持Map方式获取
     *
     * @param property property 如果属性名含有点号则嵌套查找属性
     * @param object   object
     * @return Object
     */
    protected static Object getPropertyValue(String property, Object object) {
        // 如果属性名含有点号则嵌套查找属性
        if (property.indexOf(POINT_NUMBER) > 0) {
            String pName = property.substring(property.indexOf(POINT_NUMBER) + 1);
            Object obj = getPropertyValue(property.substring(0, property.indexOf(POINT_NUMBER)), object);
            return getPropertyValue(pName, obj);
        }
        if (object instanceof Map) {
            return ((Map) object).get(property);
        }
        Object fieldValue = null;
        try {
            String methodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
            Method method = object.getClass().getMethod(methodName, new Class[0]);

            /*
             * PropertyDescriptor pd = new PropertyDescriptor(property,
             * object.getClass()); Method getMethod = pd.getReadMethod();//
             * 取出当字段的get方法. fieldValue = getMethod.invoke(object, new
             * Object[0]);
             */
            fieldValue = method.invoke(object, new Object[0]);
        } catch (Exception e) {
            LOGGER.error("Occurs an unexpected exception",e);
        }
        return fieldValue;
    }

    /**
     * findAttribute 嵌套查找上下文属性
     *
     * @param name 属性名，可以用.号嵌套
     * @return 属性值
     */
    protected Object findAttribute(String name) {
        Object attr = tagContext.getBeans().get(name);
        if (attr == null && name.indexOf(".") > 0) {
            Object obj = tagContext.getBeans().get(name.substring(0, name.indexOf(".")));
            String attrName = name.substring(name.indexOf(".") + 1);
            if (obj != null) {
                attr = getPropertyValue(attrName, obj);
            }
        }
        return attr;
    }

    /**
     * getExprValue 根据表达式计算值
     * 自动判断是否有${}，有则使用表达式计算。含有“.”也尝试用表达式解析
     *
     * @param expr 表达式
     * @return 表达式的值
     */
    public Object getExprValue(String expr) {
        Object result = null;
        if (expr.matches("\\$\\{[\\s\\S.]+\\}")) {
            ExpressionParser exprParser = new ExpressionParser(expr, tagContext.getBeans(), tagContext.getSheet().getConfiguration());
            Expression testExpr = exprParser.parse();
            try {
                result = testExpr.evaluate();
            } catch (Exception e) {
                LOGGER.error("Can't evaluate expression: " + expr, e);
                throw new RuntimeException("Can't evaluate test expression: " + expr, e);
            }
        } else if (expr.indexOf(POINT_NUMBER) > 0) {
            try {
                result = new Expression(expr, tagContext.getBeans(), tagContext.getSheet().getConfiguration()).evaluate();
            } catch (Exception e) {
                LOGGER.error("Can't evaluate expression: " + expr, e);
                throw new RuntimeException("Can't evaluate test expression: " + expr, e);
            }
        } else {
            result = expr;
        }
        return result;
    }


}
