package org.fortune.commons.export2.element.excel.factory;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.fortune.commons.export2.domain.ExportColumnWidth;
import org.springframework.util.StringUtils;

public class SheetFactory {

    private Workbook workbook;

    public SheetFactory(Workbook workbook) {
        this.workbook = workbook;
    }

    public Sheet createSheet(String name, List<ExportColumnWidth> colWidths) {
        Sheet sheet = null;

        if (!StringUtils.isEmpty(name)) {
            sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName(name));
        } else {
            sheet = workbook.createSheet();
        }

        sheet.setDisplayGridlines(false);

        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setFitWidth((short) 1);
        sheet.getPrintSetup().setFitHeight((short) 0);

        sheet.setHorizontallyCenter(true);
        sheet.setMargin(Sheet.TopMargin, 0.2);
        sheet.setMargin(Sheet.RightMargin, 0.2);
        sheet.setMargin(Sheet.BottomMargin, 0.2);
        sheet.setMargin(Sheet.LeftMargin, 0.2);
        sheet.setAutobreaks(true);
        sheet.setFitToPage(true);

        for (ExportColumnWidth colWidth : colWidths) {
            sheet.setColumnWidth(colWidth.getIndex(), colWidth.getWidth());
        }

        return sheet;
    }

}
