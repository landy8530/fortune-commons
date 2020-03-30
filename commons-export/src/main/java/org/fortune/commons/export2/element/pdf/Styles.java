package org.fortune.commons.export2.element.pdf;

import com.itextpdf.layout.Style;
import org.fortune.commons.export2.element.pdf.factory.StyleFactory;

import java.util.Map;

public class Styles {

    private Style normalStyle;
    private Style boldStyle;
    private Style titleStyle;
    private Style headerStyle;
    private Style dataStyle;

    public Styles(Map<String, Integer> fontSize) {
        StyleFactory styleFactory = new StyleFactory(fontSize);
        normalStyle = styleFactory.createNormalStyle();
        boldStyle = styleFactory.createBoldStyle();
        titleStyle = styleFactory.createTitleStyle();
        headerStyle = styleFactory.createHeaderStyle();
        dataStyle = styleFactory.createDataStyle();
    }

    public Style getNormalStyle() {
        return normalStyle;
    }

    public Style getBoldStyle() {
        return boldStyle;
    }

    public Style getTitleStyle() {
        return titleStyle;
    }

    public Style getHeaderStyle() {
        return headerStyle;
    }

    public Style getDataStyle() {
        return dataStyle;
    }

}
