package org.fortune.commons.crawler.parser;

import org.fortune.commons.crawler.annotation.PageFieldSelect;
import org.fortune.commons.crawler.annotation.PageSelect;
import org.fortune.commons.crawler.enums.SelectType;
import org.fortune.commons.crawler.util.FieldReflectionUtil;
import org.fortune.commons.crawler.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/22 23:52
 * @description: HTML页面统一解析处理器
 */
public class PageParserProcessor {
    private static Logger logger = LoggerFactory.getLogger(PageParserProcessor.class);

    /**
     * 处理具体的页面HTML 文档内容解析
     * @param document Jsoup文档对象
     * @param pageParser 页面解析器对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static boolean processPage(Document document, PageParser pageParser) throws IllegalAccessException, InstantiationException {
        if (document == null) {
            return false;
        }

        // pagevo class-field info
        Class pageVoClassType = Object.class;

        Type pageVoParserClass = pageParser.getClass().getGenericSuperclass();
        if (pageVoParserClass instanceof ParameterizedType) {
            Type[] pageVoClassTypes = ((ParameterizedType)pageVoParserClass).getActualTypeArguments();
            pageVoClassType = (Class) pageVoClassTypes[0];
        }

        PageSelect pageVoSelect = (PageSelect) pageVoClassType.getAnnotation(PageSelect.class);
        String pageVoCssQuery = (pageVoSelect!=null && pageVoSelect.cssQuery()!=null && pageVoSelect.cssQuery().trim().length()>0)?pageVoSelect.cssQuery():"html";

        // pagevo document to object
        Elements pageVoElements = document.select(pageVoCssQuery);

        if (pageVoElements != null && pageVoElements.hasText()) {
            for (Element pageVoElement : pageVoElements) {

                Object pageVo = pageVoClassType.newInstance();

                Field[] fields = pageVoClassType.getDeclaredFields();
                if (fields!=null) {
                    for (Field field: fields) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }
                        // field origin value
                        PageFieldSelect fieldSelect = field.getAnnotation(PageFieldSelect.class);
                        String cssQuery = null;
                        SelectType selectType = null;
                        String selectVal = null;
                        if (fieldSelect != null) {
                            cssQuery = fieldSelect.cssQuery();
                            selectType = fieldSelect.selectType();
                            selectVal = fieldSelect.selectVal();
                        }
                        if (cssQuery==null || cssQuery.trim().length()==0) {
                            continue;
                        }

                        // field value
                        Object fieldValue = null;

                        if (field.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
                            if (fieldGenericType.getRawType().equals(List.class)) {

                                //Type gtATA = fieldGenericType.getActualTypeArguments()[0];
                                Elements fieldElementList = pageVoElement.select(cssQuery);
                                if (fieldElementList!=null && fieldElementList.size()>0) {

                                    List<Object> fieldValueTmp = new ArrayList<Object>();
                                    for (Element fieldElement: fieldElementList) {

                                        String fieldElementOrigin = JsoupUtil.parseElement(fieldElement, selectType, selectVal);
                                        if (fieldElementOrigin==null || fieldElementOrigin.length()==0) {
                                            continue;
                                        }
                                        try {
                                            fieldValueTmp.add(FieldReflectionUtil.parseValue(field, fieldElementOrigin));
                                        } catch (Exception e) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }

                                    if (fieldValueTmp.size() > 0) {
                                        fieldValue = fieldValueTmp;
                                    }
                                }
                            }
                        } else {

                            Elements fieldElements = pageVoElement.select(cssQuery);
                            String fieldValueOrigin = null;
                            if (fieldElements!=null && fieldElements.size()>0) {
                                fieldValueOrigin = JsoupUtil.parseElement(fieldElements.get(0), selectType, selectVal);
                            }

                            if (fieldValueOrigin==null || fieldValueOrigin.length()==0) {
                                continue;
                            }

                            try {
                                fieldValue = FieldReflectionUtil.parseValue(field, fieldValueOrigin);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }

                        if (fieldValue!=null) {
                            /*PropertyDescriptor pd = new PropertyDescriptor(field.getName(), pageVoClassType);
                            Method method = pd.getWriteMethod();
                            method.invoke(pageVo, fieldValue);*/

                            field.setAccessible(true);
                            field.set(pageVo, fieldValue);
                        }
                    }
                }

                // pagevo output
                pageParser.parse(document, pageVoElement, pageVo);
            }
        }

        return true;
    }

    public static boolean processPage(String html, PageParser pageParser) throws IllegalAccessException, InstantiationException {
        Document document = Jsoup.parse(html);
        return processPage(document, pageParser);
    }
}
