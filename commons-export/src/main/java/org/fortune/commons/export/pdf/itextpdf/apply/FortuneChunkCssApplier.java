package org.fortune.commons.export.pdf.itextpdf.apply;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import org.fortune.commons.core.util.StringUtil;

import java.util.Map;

/**
 * @author: Landy
 * @date: 2019/4/6 13:03
 * @description:
 */
public class FortuneChunkCssApplier extends ChunkCssApplier {

    private Font applyFontStylesOfChinese(final Tag t) {
        float size = new FontSizeTranslator().getFontSize(t);
        int style = Font.UNDEFINED;
        BaseColor color = null;
        Map<String, String> rules = t.getCSS();
        for (Map.Entry<String, String> entry : rules.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (CSS.Property.FONT_WEIGHT.equalsIgnoreCase(key)) {
                if (CSS.Value.BOLD.contains(value)) {
                    if (style == Font.ITALIC) {
                        style = Font.BOLDITALIC;
                    } else {
                        style = Font.BOLD;
                    }
                } else {
                    if (style == Font.BOLDITALIC) {
                        style = Font.ITALIC;
                    } else {
                        style = Font.NORMAL;
                    }
                }
            } else if (CSS.Property.FONT_STYLE.equalsIgnoreCase(key)) {
                if (value.equalsIgnoreCase(CSS.Value.ITALIC)) {
                    if (style == Font.BOLD) {
                        style = Font.BOLDITALIC;
                    } else {
                        style = Font.ITALIC;
                    }
                }
            } else if (CSS.Property.FONT_FAMILY.equalsIgnoreCase(key)) {
            } else if (CSS.Property.COLOR.equalsIgnoreCase(key)) {
                color = HtmlUtilities.decodeColor(value);
            }
        }
        return fontProvider.getFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED, size, style, color);
    }

    public Chunk apply(final Chunk c, final Tag t) {
        Font f;
        if (StringUtil.containsChineseChar(c.getContent())) {
            f = applyFontStylesOfChinese(t);
        } else {
            f = applyFontStyles(t);
        }
        float size = f.getSize();
        Map<String, String> rules = t.getCSS();
        for (Map.Entry<String, String> entry : rules.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (CSS.Property.FONT_STYLE.equalsIgnoreCase(key)) {
                if (value.equalsIgnoreCase(CSS.Value.OBLIQUE)) {
                    c.setSkew(0, 12);
                }
            } else if (CSS.Property.LETTER_SPACING.equalsIgnoreCase(key)) {
                c.setCharacterSpacing(utils.parsePxInCmMmPcToPt(value));
            } else if (null != rules.get(CSS.Property.XFA_FONT_HORIZONTAL_SCALE)) { // only % allowed; need a catch block NumberFormatExc?
                c.setHorizontalScaling(Float.parseFloat(rules.get(CSS.Property.XFA_FONT_HORIZONTAL_SCALE).replace("%", "")) / 100);
            }
        }
        // following styles are separate from the for each loop, because they are based on font settings like size.
        if (null != rules.get(CSS.Property.VERTICAL_ALIGN)) {
            String value = rules.get(CSS.Property.VERTICAL_ALIGN);
            if (value.equalsIgnoreCase(CSS.Value.SUPER) || value.equalsIgnoreCase(CSS.Value.TOP) || value.equalsIgnoreCase(CSS.Value.TEXT_TOP)) {
                c.setTextRise((float) (size / 2 + 0.5));
            } else if (value.equalsIgnoreCase(CSS.Value.SUB) || value.equalsIgnoreCase(CSS.Value.BOTTOM) || value.equalsIgnoreCase(CSS.Value.TEXT_BOTTOM)) {
                c.setTextRise(-size / 2);
            } else {
                c.setTextRise(utils.parsePxInCmMmPcToPt(value));
            }
        }
        String xfaVertScale = rules.get(CSS.Property.XFA_FONT_VERTICAL_SCALE);
        if (null != xfaVertScale) {
            if (xfaVertScale.contains("%")) {
                size *= Float.parseFloat(xfaVertScale.replace("%", "")) / 100;
                c.setHorizontalScaling(100 / Float.parseFloat(xfaVertScale.replace("%", "")));
            }
        }
        if (null != rules.get(CSS.Property.TEXT_DECORATION)) { // Restriction? In html a underline and a line-through is possible on one piece of text. A Chunk can set an underline only once.
            String value = rules.get(CSS.Property.TEXT_DECORATION);
            if (CSS.Value.UNDERLINE.equalsIgnoreCase(value)) {
                c.setUnderline(0.75f, -size / 8f);
            }
            if (CSS.Value.LINE_THROUGH.equalsIgnoreCase(value)) {
                c.setUnderline(0.75f, size / 4f);
            }
        }
        if (null != rules.get(CSS.Property.BACKGROUND_COLOR)) {
            c.setBackground(HtmlUtilities.decodeColor(rules.get(CSS.Property.BACKGROUND_COLOR)));
        }
        f.setSize(size);
        c.setFont(f);

        Float leading = null;
        if (rules.get(CSS.Property.LINE_HEIGHT) != null) {
            String value = rules.get(CSS.Property.LINE_HEIGHT);
            if (utils.isNumericValue(value)) {
                leading = Float.parseFloat(value) * c.getFont().getSize();
            } else if (utils.isRelativeValue(value)) {
                leading = utils.parseRelativeValue(value, c.getFont().getSize());
            } else if (utils.isMetricValue(value)) {
                leading = utils.parsePxInCmMmPcToPt(value);
            }
        }

        if (leading != null) {
            c.setLineHeight(leading);
        }
        return c;

    }
}