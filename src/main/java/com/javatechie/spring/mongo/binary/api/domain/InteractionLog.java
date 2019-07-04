package com.javatechie.spring.mongo.binary.api.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


//@Document(indexName = "gridfs_activities", type = "gridfsLogs")
@Document(collection = "gridfs_activities")
public class InteractionLog {

    @Field("import_code")
    private String importCode;


    @Field("tenant_id")
    private Integer tenantId;


    @Field("date_import")
    private Date dateImport;


    @Field("tenant_uuid")
    private String tenantUuid;


    @Field("thread_id")
    private Integer threadId;


    @Field("mail_id")
    private Integer mailId;


    @Field("parsed_mail_id")
    private Integer parsedMailId;


    @Field("attached_file_size")
    private long attachedFileSize;


    @Field("attached_file_name")
    private String attachedFileName;

    public InteractionLog( Date dateImport, Integer tenantId,
                           String tenantUuid, Integer threadId,
                           Integer mailId, Integer parsedMailId,
                           long attachedFileSize, String attachedFileName, String importCode) {
        this.tenantId = tenantId;
        this.dateImport = dateImport;
        this.tenantUuid = tenantUuid;
        this.threadId = threadId;
        this.mailId = mailId;
        this.parsedMailId = parsedMailId;
        this.attachedFileSize = attachedFileSize;
        this.attachedFileName = attachedFileName;
        this.importCode = importCode;
    }

    public InteractionLog() {
        super();
    }


    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Date getDateImport() {
        return dateImport;
    }

    public void setDateImport(Date dateImport) {
        this.dateImport = dateImport;
    }

    public String getTenantUuid() {
        return tenantUuid;
    }

    public void setTenantUuid(String tenantUuid) {
        this.tenantUuid = tenantUuid;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public Integer getMailId() {
        return mailId;
    }

    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    public Integer getParsedMailId() {
        return parsedMailId;
    }

    public void setParsedMailId(Integer parsedMailId) {
        this.parsedMailId = parsedMailId;
    }

    public long getAttachedFileSize() {
        return attachedFileSize;
    }

    public void setAttachedFileSize(long attachedFileSize) {
        this.attachedFileSize = attachedFileSize;
    }

    public String getAttachedFileName() {
        return attachedFileName;
    }

    public void setAttachedFileName(String attachedFileName) {
        this.attachedFileName = attachedFileName;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }
}
