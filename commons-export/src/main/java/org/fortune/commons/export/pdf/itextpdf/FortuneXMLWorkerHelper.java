package org.fortune.commons.export.pdf.itextpdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.css.*;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.fortune.commons.export.pdf.itextpdf.apply.FortuneChunkCssApplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * @author: Landy
 * @date: 2019/4/6 12:53
 * @description:
 */
public class FortuneXMLWorkerHelper {

    /**
     * Get a Singleton XMLWorkerHelper
     *
     * @return a singleton instance of XMLWorkerHelper
     * @author Landy
     * @date 2019/4/6 13:01
     */
    public synchronized static final FortuneXMLWorkerHelper getInstance() {
        //在返回结果以前，一定会先加载内部类
        return MyXMLWorkerHelperHolder.INSTANCE;
    }

    private static class MyXMLWorkerHelperHolder {
        private static final FortuneXMLWorkerHelper INSTANCE = new FortuneXMLWorkerHelper();
    }

    private TagProcessorFactory tpf;
    private CssFile defaultCssFile;

    private FortuneXMLWorkerHelper() {}

    /**
     * @return the default css file.
     */
    public static synchronized CssFile getCSS(InputStream in) {
        CssFile cssFile = null;
        if (null != in) {
            final CssFileProcessor cssFileProcessor = new CssFileProcessor();
            int i = -1;
            try {
                while (-1 != (i = in.read())) {
                    cssFileProcessor.process((char) i);
                }
                cssFile = new CSSFileWrapper(cssFileProcessor.getCss(), true);
            } catch (final IOException e) {
                throw new RuntimeWorkerException(e);
            } finally {
                try {
                    in.close();
                } catch (final IOException e) {
                    throw new RuntimeWorkerException(e);
                }
            }
        }

        return cssFile;
    }

    public synchronized CssFile getDefaultCSS() {
        if (null == defaultCssFile) {
            defaultCssFile = getCSS(XMLWorkerHelper.class.getResourceAsStream("/default.css"));
        }
        return defaultCssFile;
    }

    /**
     * Parses the xml data in the given reader and sends created {@link Element}
     * s to the defined ElementHandler.<br />
     * This method configures the XMLWorker and XMLParser to parse (X)HTML/CSS
     * and accept unknown tags.
     *
     * @param d  the handler
     * @param in the reader
     * @throws IOException thrown when something went wrong with the IO
     */
    public void parseXHtml(final ElementHandler d, final Reader in) throws IOException {
//        CssFilesImpl cssFiles = new CssFilesImpl();
//        cssFiles.add(getDefaultCSS());
//        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
//        HtmlPipelineContext hpc = new HtmlPipelineContext(null);
//        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
//        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d,
//                null)));
//        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLWorker worker = getXMLWorker(d);
        XMLParser p = new XMLParser();
        p.addListener(worker);
        p.parse(in);
    }

    /**
     * Parses the xml data. This method configures the XMLWorker to parse
     * (X)HTML/CSS and accept unknown tags. Writes the output in the given
     * PdfWriter with the given document.
     *
     * @param writer the PdfWriter
     * @param doc    the Document
     * @param in     the reader
     * @throws IOException thrown when something went wrong with the IO
     */
    public void parseXHtml(final PdfWriter writer, final Document doc, final Reader in) throws IOException {
        CssFilesImpl cssFiles = new CssFilesImpl();
        cssFiles.add(getDefaultCSS());
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(getCssAppliers());
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc,
                writer)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser();
        p.addListener(worker);
        p.parse(in);
    }

    /**
     * @param writer the writer to use
     * @param doc    the document to use
     * @param in     the {@link InputStream} of the XHTML source.
     * @throws IOException if the {@link InputStream} could not be read.
     */
    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in) throws IOException {
        parseXHtml(writer, doc, in, XMLWorkerHelper.class.getResourceAsStream("/default.css"), null, new XMLWorkerFontProvider());
    }

    /**
     * @param writer  the writer to use
     * @param doc     the document to use
     * @param in      the {@link InputStream} of the XHTML source.
     * @param charset the charset to use
     * @throws IOException if the {@link InputStream} could not be read.
     */
    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final Charset charset) throws IOException {
        parseXHtml(writer, doc, in, XMLWorkerHelper.class.getResourceAsStream("/default.css"), charset);
    }

    /**
     * @param writer  the writer to use
     * @param doc     the document to use
     * @param in      the {@link InputStream} of the XHTML source.
     * @param in      the {@link CssFiles} of the css files.
     * @param charset the charset to use
     * @throws IOException if the {@link InputStream} could not be read.
     */
    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final Charset charset, final FontProvider fontProvider) throws IOException {
        CssFilesImpl cssFiles = new CssFilesImpl();
        if (inCssFile != null)
            cssFiles.add(getCSS(inCssFile));
        else
            cssFiles.add(getDefaultCSS());
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(getCssAppliers());
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
        HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(true, worker, charset);
        if (charset != null)
            p.parse(in, charset);
        else
            p.parse(in);
    }

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, null, new XMLWorkerFontProvider());
    }

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final FontProvider fontProvider) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, null, fontProvider);
    }

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final Charset charset) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, charset, new XMLWorkerFontProvider());
    }

    /**
     * @param d       the ElementHandler
     * @param in      the InputStream
     * @param charset the charset to use
     * @throws IOException if something went seriously wrong with IO.
     */
    public void parseXHtml(final ElementHandler d, final InputStream in, final Charset charset) throws IOException {
//        CssFilesImpl cssFiles = new CssFilesImpl();
//        cssFiles.add(getDefaultCSS());
//        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
//        HtmlPipelineContext hpc = new HtmlPipelineContext(null);
//        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
//        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d,
//                null)));
        XMLWorker worker = getXMLWorker(d);
        XMLParser p = new XMLParser(true, worker, charset);
        if (charset != null)
            p.parse(in, charset);
        else
            p.parse(in);
    }

    /**
     * Get a CSSResolver implementation.
     *
     * @param addDefaultCss true if the defaultCss should already be added.
     * @return the default CSSResolver
     */
    public CSSResolver getDefaultCssResolver(final boolean addDefaultCss) {
        CSSResolver resolver = new StyleAttrCSSResolver();
        if (addDefaultCss) {
            resolver.addCss(getDefaultCSS());
        }
        return resolver;
    }

    /**
     * Retrieves the default factory for processing HTML tags from
     * {@link Tags#getHtmlTagProcessorFactory()}. On subsequent calls the same
     * {@link TagProcessorFactory} is returned every time. <br />
     *
     * @return a
     * <code>DefaultTagProcessorFactory<code> that maps HTML tags to {@link TagProcessor}s
     */
    protected synchronized TagProcessorFactory getDefaultTagProcessorFactory() {
        if (null == tpf) {
            tpf = Tags.getHtmlTagProcessorFactory();
        }
        return tpf;
    }

    private CssAppliersImpl getCssAppliers() {
        CssAppliersImpl cssAppliers = new CssAppliersImpl();
        cssAppliers.putCssApplier(Chunk.class,new FortuneChunkCssApplier()); //支持中文处理
        return cssAppliers;
    }

    private XMLWorker getXMLWorker(final ElementHandler d) {
        CssFilesImpl cssFiles = new CssFilesImpl();
        cssFiles.add(getDefaultCSS());
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(null);
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d,
                null)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        return worker;
    }
}
