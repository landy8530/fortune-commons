package org.fortune.commons.export2.element.excel.factory;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.fortune.commons.export2.element.excel.Colors;

public class FontFactory {

    public static final String CALIBRI = "Calibri";

    public static final short NINE = 9;
    public static final short TEN = 10;
    public static final short FOURTEEN = 14;

    public static final short NORMAL = 0;
    public static final short BOLD = 1;
    public static final short ITALIC = 2;

    private Workbook workbook;

    public FontFactory(Workbook workbook) {
        this.workbook = workbook;
    }

    public Font createFont(String name, short size, short style, short color) {
        Font font = createFont(name, size, style);
        font.setColor(color);

        return font;
    }

    public Font createFont(String name, short size, short style) {
        Font font = workbook.createFont();
        font.setFontName(name);
        font.setFontHeightInPoints(size);
        setStyle(font, style);
        font.setColor(Colors.BLACK);

        return font;
    }

    private void setStyle(Font font, short style) {
        switch (style) {
            case NORMAL:
                break;

            case BOLD:
                font.setBold(true);
                break;

            case ITALIC:
                font.setItalic(true);
                break;

            default:
        }
    }

}
