package org.landy.commons.web.springmvc.form.support;

import org.landy.commons.web.springmvc.form.BaseForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.ui.ModelMap;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Landy
 * @date: 2019/4/20 17:25
 * @description:
 */
public class InvokerFormPropertyValueToModelMapUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokerFormPropertyValueToModelMapUtil.class);

    public static void invokerPropertyValToModelMap(BaseForm form, ModelMap modelMap){
        PropertyDescriptor[] props= BeanUtils.getPropertyDescriptors(form.getClass());
        String name;
        for(PropertyDescriptor pd:props){
            try {
                name=pd.getName();
                if(!modelMap.containsKey(name)){
                    Object val=pd.getReadMethod().invoke(form, new Object[]{});
                    if(val!=null){
                        modelMap.put(pd.getName(), val);
                    }
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("Occurs an unexpected exception",e);
            } catch (IllegalArgumentException e) {
                LOGGER.error("Occurs an unexpected exception",e);
            } catch (InvocationTargetException e) {
                LOGGER.error("Occurs an unexpected exception",e);
            }
        }
    }

}
