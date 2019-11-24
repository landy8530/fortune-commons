package org.fortune.commons.core.util;

import com.sun.org.apache.xpath.internal.XPathAPI;
import com.sun.org.apache.xpath.internal.objects.XObject;
import org.fortune.commons.core.exception.XMLException;
import org.w3c.dom.*;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/21 22:18
 * @description:
 */
public class XPathUtils {

    public static Element selectElement(Node root, String xpath) {
        try {
            Node node = XPathAPI.selectSingleNode(root, xpath);
            if (node == null) {
                return null;
            } else if (!(node instanceof Element)) {
                throw new XMLException("target node is not element, xpath=" + xpath + ", element=" + DOMUtils.text(root));
            } else {
                return (Element)node;
            }
        } catch (TransformerException var3) {
            throw new XMLException(var3);
        }
    }

    public static List<Element> selectElements(Node root, String xpath) {
        ArrayList elements = new ArrayList();

        try {
            NodeList nodes = XPathAPI.selectNodeList(root, xpath);

            for(int i = 0; i < nodes.getLength(); ++i) {
                Node node = nodes.item(i);
                if (!(node instanceof Element)) {
                    throw new XMLException("target node is not element, xpath=" + xpath + ", element=" + DOMUtils.text(root));
                }

                elements.add((Element)node);
            }

            return elements;
        } catch (TransformerException var6) {
            throw new XMLException(var6);
        }
    }

    public static String selectText(Node root, String xpath) {
        try {
            Node node = XPathAPI.selectSingleNode(root, xpath);
            if (node == null) {
                return null;
            } else if (node instanceof Text) {
                return node.getTextContent();
            } else if (node instanceof Element) {
                return DOMUtils.getText((Element)node);
            } else if (node instanceof Attr) {
                return ((Attr)node).getValue();
            } else {
                throw new XMLException("unsupported type, xpath=" + xpath + ", element=" + DOMUtils.text(root));
            }
        } catch (TransformerException var3) {
            throw new XMLException(var3);
        }
    }

    public static int selectInt(Node root, String xpath) {
        try {
            XObject result = XPathAPI.eval(root, xpath);
            return (int)result.num();
        } catch (TransformerException var3) {
            throw new XMLException(var3);
        }
    }

    private XPathUtils() {
    }

}
