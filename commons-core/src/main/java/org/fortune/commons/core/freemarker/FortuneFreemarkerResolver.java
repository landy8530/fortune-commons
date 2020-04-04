package org.fortune.commons.core.freemarker;

import freemarker.template.*;
import org.fortune.commons.core.constants.Constants;
import org.fortune.commons.core.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 19:43
 * @description:
 */
public class FortuneFreemarkerResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(FortuneFreemarkerResolver.class);

    private Configuration configuration = null;

    private Map<String, Object> extMapParams;

    private String rootPath;

    private static final String DEFAULT_ROOT_PATH = "/templates";

    //保证单例线程安全的同时，也保证不被反射机制破坏
    private boolean initialized = false;

    public synchronized static final FortuneFreemarkerResolver getInstance() {
        //在返回结果以前，一定会先加载内部类
        return FortuneFreemarkerResolverHolder.INSTANCE;
    }

    private static class FortuneFreemarkerResolverHolder {
        private static final FortuneFreemarkerResolver INSTANCE = new FortuneFreemarkerResolver();
    }

    private FortuneFreemarkerResolver() {
        synchronized (FortuneFreemarkerResolver.class) {
            if(initialized) {
                throw new RuntimeException("单例初始化Ftl解析类被破坏");
            } else {
                initialized = true;
                LOGGER.info("初始化Ftl解析类");
            }
        }
    }

    public synchronized void init() {
        if (configuration == null) {
            LOGGER.info("初始化FTL上下文信息");
            configuration = new Configuration(Configuration.VERSION_2_3_28);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28));
            configuration.setClassicCompatible(true);
            configuration.setDefaultEncoding(Constants.ENCODING_UTF_8);
            configuration.setEncoding(Locale.getDefault(), Constants.ENCODING_UTF_8);
            try {
                File ftlRootPath = new File(getRootPath());
                LOGGER.info("设置FTL资源路径：" + ftlRootPath.getAbsolutePath() + ",是否存在：" + ftlRootPath.exists());
                configuration.setDirectoryForTemplateLoading(ftlRootPath);
            } catch (IOException e) {
                LOGGER.error("初始化FTL上下文信息失败", e);
            }
        }
    }

    /**
     * 生成静态化文件
     *
     * @param myResourceData
     * @return
     * @author: Landy
     */
    public String process(FtlResourceData myResourceData) {
        if (configuration == null) {
            init();
        }
        Map<String, Object> root = myResourceData.getData();

        //加入静态资源信息
        if (this.getExtMapParams() != null) {
            root.putAll(this.extMapParams);
        }
        String ftlPath = myResourceData.getFtlTemplatePath();
        String fileName = ftlPath;
        Template temp;
        Writer out = null;
        try {
            temp = configuration.getTemplate(fileName);
            out = new StringWriter();
            //设置不执行数值格式转化-解决逗号问题
            temp.setNumberFormat("#.##");
            temp.process(root, out);
            out.flush();
            return out.toString();

        } catch (IOException e) {
            LOGGER.error("Occurs an unexpected exception", e);
        } catch (TemplateException e) {
            LOGGER.error("Occurs an unexpected exception", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }

        return "";
    }

    public Map<String, Object> getExtMapParams() {
        return extMapParams;
    }

    public void setExtMapParams(Map<String, Object> extMapParams) {
        this.extMapParams = extMapParams;
    }

    public String getRootPath() {
        //如果没有显示设置根路径，则默认取class path下的template目录
        if(!StringUtils.hasText(rootPath)) {
            //设置根路径
            String classPath = FileUtil.getFilePathByClassPath(DEFAULT_ROOT_PATH);
            rootPath = classPath;
        }
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}
