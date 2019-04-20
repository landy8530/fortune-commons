package org.landy.commons.export.excel.jxls;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 12:17
 * @description: 利用JXLS模版导出Excel文件Handler
 */
public class ExportXlsHandler {

    private  final Logger LOG = LoggerFactory.getLogger(ExportXlsHandler.class);


    public String cleanPath(String path) {
        try {
            path = path.replaceAll("\\\\", "/");
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void execute(OutputStream outputStream , String templateFilePath, Map<String,Object> data){
        InputStream is;
        Assert.notNull(templateFilePath, "模板文件不为空");
        Assert.notNull(outputStream, "输出流不为空");
        Assert.notNull(data, "数据不为空");
        try {
            is = new BufferedInputStream(new FileInputStream(cleanPath(templateFilePath)));
        } catch (FileNotFoundException e1) {
            LOG.error("导出模板文件不存在,templateFilePath={}",templateFilePath);
            return;
        }
        // 将action中的属性转成map的键值
        XLSTransformer transformer = new XLSTransformer();
        Workbook workBook;
        // 导出
        try {
            workBook = transformer.transformXLS(is, data);
            workBook.write(outputStream);
            outputStream.flush();
        } catch (Throwable e) {
            LOG.error("导出EXCEL出错,templateFilePath={}",templateFilePath,e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("导出EXCEL出错：" + e);
            }
        }
    }

}
