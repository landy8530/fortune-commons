package org.fortune.commons.export2.element.excel;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.fortune.commons.export2.element.excel.factory.FontFactory;

public class Fonts {

    private Font normalFont;
    private Font boldFont;
    private Font boldWhiteFont;
    private Font boldGreenFont;

    public Fonts(Workbook workbook) {
        FontFactory fontFactory = new FontFactory(workbook);
        normalFont = fontFactory.createFont(FontFactory.CALIBRI, FontFactory.TEN, FontFactory.NORMAL);
        boldFont = fontFactory.createFont(FontFactory.CALIBRI, FontFactory.TEN, FontFactory.BOLD);
        boldWhiteFont = fontFactory.createFont(FontFactory.CALIBRI, FontFactory.NINE, FontFactory.BOLD, Colors.WHITE);
        boldGreenFont = fontFactory.createFont(FontFactory.CALIBRI, FontFactory.FOURTEEN, FontFactory.BOLD, Colors.GREEN);
    }

    public Font getNormalFont() {
        return normalFont;
    }

    public Font getBoldFont() {
        return boldFont;
    }

    public Font getBoldGreenFont() {
        return boldGreenFont;
    }

    public Font getBoldWhiteFont() {
        return boldWhiteFont;
    }

}
