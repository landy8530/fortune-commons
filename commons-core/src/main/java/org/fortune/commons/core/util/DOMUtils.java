package org.fortune.commons.core.util;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.fortune.commons.core.exception.XMLException;
import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: landy
 * @date: 2019/11/24 15:26
 * @description:
 */
public class DOMUtils {

    private static final TransformerFactory TRANSFORMER_FACTORY = new TransformerFactoryImpl();

    public static String text(Node node) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        print(node, bytes);
        return new String(bytes.toByteArray(), Charset.forName("UTF-8"));
    }

    private static void print(Node node, OutputStream out) {
        try {
            Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(new DOMSource(node), new StreamResult(out));
        } catch (TransformerException var3) {
            throw new XMLException(var3);
        }
    }

    public static List<Element> childElements(Node node) {
        List<Element> result = new ArrayList();
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();

        for(int i = 0; i < length; ++i) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                result.add((Element)child);
            }
        }

        return result;
    }

    public static List<Node> children(Node node) {
        List<Node> result = new ArrayList();
        NodeList childNodes = node.getChildNodes();
        int length = childNodes.getLength();

        for(int i = 0; i < length; ++i) {
            Node child = childNodes.item(i);
            if (child instanceof Text) {
                Text text = (Text)child;
                if (StringUtil.hasText(text.getWholeText())) {
                    result.add(text);
                }
            } else {
                result.add(child);
            }
        }

        return result;
    }

    public static List<Attr> attributes(Node node) {
        List<Attr> result = new ArrayList();
        NamedNodeMap attributes = node.getAttributes();
        int length = attributes.getLength();

        for(int i = 0; i < length; ++i) {
            Attr attr = (Attr)attributes.item(i);
            result.add(attr);
        }

        return result;
    }

    public static void setText(Element element, String text) {
        NodeList childNodes = element.getChildNodes();
        if (childNodes.getLength() > 1) {
            throw new XMLException("can not set text for element " + text(element));
        } else {
            if (childNodes.getLength() == 0) {
                Text textNode = element.getOwnerDocument().createTextNode(text);
                element.appendChild(textNode);
            } else {
                Node textNode = element.getFirstChild();
                if (!(textNode instanceof Text)) {
                    throw new XMLException("can not set text for element " + text(element));
                }

                textNode.setTextContent(text);
            }

        }
    }

    public static String getText(Element element) {
        NodeList children = element.getChildNodes();
        if (children.getLength() == 0) {
            return element.getTextContent();
        } else {
            if (children.getLength() == 1) {
                Node firstChild = element.getFirstChild();
                if (firstChild instanceof Text) {
                    return firstChild.getTextContent();
                }
            }

            throw new XMLException("target element is not a text element " + text(element));
        }
    }

    public static String prettyFormat(Document document) {
        try {
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setOmitXMLDeclaration(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException var4) {
            throw new XMLException(var4);
        }
    }

}
