package org.fortune.commons.export2.element.pdf;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Fonts {

    private static final Logger logger = LoggerFactory.getLogger(Fonts.class);

    private PdfFont normalFont;
    private PdfFont boldFont;
    private int titleFontSize;
    private int dataFontSize;

    public Fonts() {
        try {
            normalFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
            boldFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException e) {
            logger.error("Error occurs when creating PDF fonts:", e);
        }
    }

    public PdfFont getNormalFont() {
        return normalFont;
    }

    public PdfFont getBoldFont() {
        return boldFont;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public int getDataFontSize() {
        return dataFontSize;
    }

    public void setDataFontSize(int dataFontSize) {
        this.dataFontSize = dataFontSize;
    }
}
