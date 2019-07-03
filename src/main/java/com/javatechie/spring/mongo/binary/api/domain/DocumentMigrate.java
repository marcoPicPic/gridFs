package com.javatechie.spring.mongo.binary.api.domain;

public class DocumentMigrate {

    private long numberOfDocument = 0;
    private long sizeOfDocument = 0;
    private long time;
    private String importCode;
    private String report;

    public DocumentMigrate(long numberOfDocument, long sizeOfDocument, String importCode) {
        this.numberOfDocument = numberOfDocument;
        this.sizeOfDocument = sizeOfDocument;
        this.importCode = importCode;
    }

    public DocumentMigrate() {
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

    public void incrementNumberOfDocuments(int i) {
        numberOfDocument = numberOfDocument + i;
    }

    public void incrementSizeOfDocument(long attachedFileSize) {
        sizeOfDocument = sizeOfDocument + attachedFileSize;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }
}
