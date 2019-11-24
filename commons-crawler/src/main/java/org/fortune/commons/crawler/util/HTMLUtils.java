package org.fortune.commons.crawler.util;

import org.cyberneko.html.parsers.DOMParser;
import org.fortune.commons.core.util.XPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class HTMLUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLUtils.class);

    public static Document parse(String html) {
        try {
            DOMParser parser = new DOMParser();
            parser.setFeature("http://xml.org/sax/features/namespaces", false);
            parser.parse(new InputSource(new StringReader(html)));
            return parser.getDocument();
        } catch (SAXException e) {
            LOGGER.warn("parse HTML error", e);
        } catch (IOException e) {
            LOGGER.warn("parse HTML error", e);
        }
        return null;
    }

    public static HTMLForm parseForm(Document htmlDoc, String formName) {
        Element element = XPathUtils.selectElement(htmlDoc, "//FORM[@name='" + formName + "']");
        return buildForm(element);
    }

    public static HTMLForm parseForm(Document htmlDoc, int number) {
        List<Element> elementList = XPathUtils.selectElements(htmlDoc, "//FORM");
        if (elementList.size() == 0) {
            throw new RuntimeException("no form tag was found in html");
        } else if (number < 0) {
            throw new RuntimeException("Please enter a valid form number, invalid number=" + number);
        } else if (number > elementList.size()) {
            throw new RuntimeException("Form number exceeds, there are only " + elementList.size() + " forms");
        } else {
            return buildForm(elementList.get(number));
        }
    }

    /**
     * simple implementation to parse form
     * this implementation does not consider file upload, multiple submit button, form method=get
     *
     * @param element form element in html
     * @return HTML form object
     */
    static HTMLForm buildForm(Element element) {
        HTMLForm form = new HTMLForm();
        form.url = element.getAttribute("action");
        //parse input element and process differently, should follow http://www.w3.org/TR/html401/interact/forms.html#h-17.13.3
        List<Element> inputElements = XPathUtils.selectElements(element, "//INPUT");
        for (Element elem : inputElements) {
            if (elem.hasAttribute("disabled"))
                continue;

            String type = elem.getAttribute("type").toLowerCase();
            if ("reset".equals(type) || "button".equals(type)) {
                continue;
            } else if ("checkbox".equals(type) || "radio".equals(type)) {
                if (!elem.hasAttribute("checked"))
                    continue;
            }

            if ("image".equals(type)) {
                form.params.put(elem.getAttribute("name") + ".x", String.valueOf(new Random().nextInt(100)));
                form.params.put(elem.getAttribute("name") + ".y", String.valueOf(new Random().nextInt(100)));
            } else {
                form.params.put(elem.getAttribute("name"), elem.getAttribute("value"));
            }
        }

        List<Element> selectElements = XPathUtils.selectElements(element, "//SELECT");
        for (Element elem : selectElements) {
            if (elem.hasAttribute("disabled"))
                continue;

            List<Element> optionElements = XPathUtils.selectElements(elem, "//SELECT[@name='" + elem.getAttribute("name") + "']//OPTION");
            for (Element optionElement : optionElements) {
                if (optionElement.hasAttribute("selected")) {
                    form.params.put(elem.getAttribute("name"), optionElement.getAttribute("value"));
                }
            }
        }

        List<Element> textElements = XPathUtils.selectElements(element, "//TEXTAREA");
        for (Element textElement : textElements) {
            if (textElement.hasAttribute("disabled"))
                continue;

            form.params.put(textElement.getAttribute("name"), textElement.getNodeValue());
        }

        return form;
    }

    public static class HTMLForm {
        public String url;

        public Map<String, String> params = new HashMap<String, String>();
    }
}
