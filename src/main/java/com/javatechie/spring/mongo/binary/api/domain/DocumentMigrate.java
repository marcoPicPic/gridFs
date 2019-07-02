package com.javatechie.spring.mongo.binary.api.domain;

public class DocumentMigrate {

    private long numberOfDocument = 0;
    private long sizeOfDocument = 0;

    public DocumentMigrate(long numberOfDocument, long sizeOfDocument) {
        this.numberOfDocument = numberOfDocument;
        this.sizeOfDocument = sizeOfDocument;
    }

    public long getNumberOfDocument() {
        return numberOfDocument;
    }

    public void setNumberOfDocument(long numberOfDocument) {
        this.numberOfDocument = numberOfDocument;
    }

    public long getSizeOfDocument() {
        return sizeOfDocument;
    }

    public void setSizeOfDocument(long sizeOfDocument) {
        this.sizeOfDocument = sizeOfDocument;
    }
}
