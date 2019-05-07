package org.landy.commons.web.util;

import org.landy.commons.core.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: Landy
 * @date: 2019/4/15 23:23
 * @description:
 */
public class PathUtils {

    private static Logger LOG = LoggerFactory.getLogger(PathUtils.class);

    /**
     * JSP页面的后缀 PAGE_JSP_SUFFIX=".jsp"
     */
    private static final String PAGE_JSP_SUFFIX = ".jsp";

    private static final String STANDARD_ACTION_PKG_NAME = ".controller.";


    private static String getPathFromPkgPathName(String pkgName) {
        if (pkgName.contains(STANDARD_ACTION_PKG_NAME)) {
            String path = pkgName.substring(pkgName.lastIndexOf(STANDARD_ACTION_PKG_NAME) + STANDARD_ACTION_PKG_NAME.length());
            path = path.replaceAll("\\.", Constants.SLASH);
            if (path.lastIndexOf("\\") == -1) {
                path = path + Constants.SLASH;
            }
            return path;
        } else {
            LOG.error("controller的路径不标准，未含有【" + STANDARD_ACTION_PKG_NAME + "】，无法解析");
        }
        return Constants.SLASH;
    }


    /**
     * 从符合规则中提取路径 包含 .action.中提取路径，如com.zbs.action.demo.demo2 解析出来的根路径为 demo/demo2
     *
     * @param actionClazz
     * @param methodName
     * @return
     */
    public static String getJSPPagePath(Class<?> actionClazz, String methodName) {
        String pkgName = actionClazz.getPackage().getName();
        String path = getPathFromPkgPathName(pkgName);
        String actionSimpleClassName = actionClazz.getSimpleName();
        if (LOG.isDebugEnabled()) {
            LOG.debug("上级路径：" + path + "controllerName=" + actionSimpleClassName + ",method=" + methodName);
        }
        if (actionSimpleClassName.contains("Controller")) {
            String name = actionSimpleClassName.substring(0, actionSimpleClassName.indexOf("Controller"));
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            path = path + name + Constants.SLASH + methodName;
            if (LOG.isDebugEnabled()) {
                LOG.debug("跳转JSP路径：" + path);
            }
            return path;
        } else {
            LOG.error("Controller名称不合规范，没含有【controller】,无法进行解析路径");
            return "";
        }
    }

    /**
     * 获取<b>文件</b>的路径，按文件的层次，将文件名放在最后一个参数，返回参数如：zzz//zzz///zzz/lyx.txt==>zzz/zzz/zzz/lyx.txt
     * 动态参数，清洗掉多个反斜杠问题
     *
     * @param pathArrays 多个路径的传入
     * @return
     */
    public static String getFilePath(String... pathArrays) {
        StringBuffer sb = new StringBuffer();
        for (String path : pathArrays) {
            sb.append(path).append(Constants.SLASH);
        }
        String url = sb.substring(0, sb.length() - 1);

        return buildUrl(url);
    }

    /**
     * 获取<b>文件夹</b>的路径, 按文件夹的层次，返回参数如：zzz//zzz///zzz//lyx//==>zzz/zzz/zzz/lyx/ OR zzz//zzz///zzz//lyx==>zzz/zzz/zzz/lyx/
     *
     * @param folderPathArrays
     * @return
     */
    public static String getFolderPath(String... folderPathArrays) {
        StringBuffer sb = new StringBuffer();
        for (String path : folderPathArrays) {
            sb.append(path).append(Constants.SLASH);
        }
        String url = sb.toString();

        return buildUrl(url);
    }

    private static String buildUrl(String url) {
        String strUrl;
        if (url.startsWith(Constants.HTTP_STR) || url.startsWith(Constants.HTTP_STR.toUpperCase())) {
            strUrl = url.substring(Constants.HTTP_STR.length());
            strUrl = strUrl.replaceAll("/++", Constants.SLASH);
            url = Constants.HTTP_STR + strUrl;
        } else {
            url = url.replaceAll("/++", Constants.SLASH);
        }
        return url;
    }
}
