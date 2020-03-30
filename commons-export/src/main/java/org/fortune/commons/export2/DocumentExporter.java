package org.fortune.commons.export2;


import org.fortune.commons.export2.builder.DocumentBuilder;

public class DocumentExporter {

    private DocumentBuilder documentBuilder;

    public DocumentExporter(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public byte[] export() {
        return documentBuilder.init()
                .buildTitle()
                .buildSummary()
                .buildDetail()
                .finl()
                .getDocument();
    }

}
