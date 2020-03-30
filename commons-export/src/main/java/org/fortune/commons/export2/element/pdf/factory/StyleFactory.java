package org.fortune.commons.export2.element.pdf.factory;

import java.util.Map;

import com.itextpdf.layout.Style;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.fortune.commons.export2.constant.Constants;
import org.fortune.commons.export2.element.pdf.Borders;
import org.fortune.commons.export2.element.pdf.Colors;
import org.fortune.commons.export2.element.pdf.Fonts;

public class StyleFactory {

    private Fonts fonts;
    private Map<String, Integer> fontSizes;

    public StyleFactory(Map<String, Integer> fontSizes) {
        this.fonts = new Fonts();
        this.fontSizes = fontSizes;
    }

    public Style createNormalStyle() {
        Style normalStyle = new Style()
                .setFont(fonts.getNormalFont())
                .setFontSize(fontSizes.get(Constants.FONT_SIZE_MIDDLE));

        return normalStyle;
    }

    public Style createBoldStyle() {
        Style boldStyle = new Style()
                .setFont(fonts.getBoldFont())
                .setFontSize(fontSizes.get(Constants.FONT_SIZE_MIDDLE));

        return boldStyle;
    }

    public Style createTitleStyle() {
        Style titleStyle = new Style()
                .setFont(fonts.getBoldFont())
                .setFontSize(fontSizes.get(Constants.FONT_SIZE_LARGE))
                .setFontColor(Colors.GREEN);

        return titleStyle;
    }

    public Style createHeaderStyle() {
        Style headerStyle = new Style()
                .setFont(fonts.getBoldFont())
                .setFontSize(fontSizes.get(Constants.FONT_SIZE_SMALL))
                .setFontColor(Colors.WHITE)
                .setBackgroundColor(Colors.GREEN)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(Borders.SOLID_GREEN);

        return headerStyle;
    }

    public Style createDataStyle() {
        Style dataStyle = new Style()
                .setFont(fonts.getNormalFont())
                .setFontSize(fontSizes.get(Constants.FONT_SIZE_MIDDLE))
                .setBorder(Borders.SOLID_LIGHT_GRAY);

        return dataStyle;
    }

}
