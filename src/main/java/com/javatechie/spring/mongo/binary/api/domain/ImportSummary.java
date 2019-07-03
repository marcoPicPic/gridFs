package com.javatechie.spring.mongo.binary.api.domain;

import java.util.HashMap;
import java.util.Map;

public class ImportSummary {

    private Map<Integer, DocumentMigrate> infosByTenant;

    private String importCode;


    public ImportSummary() {
        infosByTenant = new HashMap<Integer, DocumentMigrate>();
    }

    public void add(InteractionLog current) {
        if(infosByTenant.containsKey(current.getTenantId())) {
            DocumentMigrate documentMigrate = infosByTenant.get(current.getTenantId());
            documentMigrate.incrementNumberOfDocuments(1);
            documentMigrate.incrementSizeOfDocument(current.getAttachedFileSize());
        } else {
            DocumentMigrate documentMigrate = new DocumentMigrate(1, current.getAttachedFileSize(), importCode);
            infosByTenant.put(current.getTenantId(), documentMigrate);
        }
    }

    public Map<Integer, DocumentMigrate> getInfosByTenant() {
        return infosByTenant;
    }

    public void setInfosByTenant(Map<Integer, DocumentMigrate> infosByTenant) {
        this.infosByTenant = infosByTenant;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }


}
