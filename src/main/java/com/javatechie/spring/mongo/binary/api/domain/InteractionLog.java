package com.javatechie.spring.mongo.binary.api.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Document(indexName = "gridfs_activities", type = "gridfsLogs")
public class InteractionLog {

    @Id
    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Integer)
    @JsonProperty("tenant_id")
    private Integer tenantId;

    @Field(type = FieldType.Date)
    @JsonProperty("date_import")
    private Date dateImport;

    @Field(type = FieldType.Keyword)
    @JsonProperty("tenant_uuid")
    private String tenantUuid;

    @Field(type = FieldType.Keyword)
    @JsonProperty("thread_id")
    private Integer threadId;

    @Field(type = FieldType.Keyword)
    @JsonProperty("mail_id")
    private Integer mailId;

    @Field(type = FieldType.Keyword)
    @JsonProperty("parsed_mail_id")
    private Integer parsedMailId;


    public InteractionLog( Date dateImport,Integer tenantId, String tenantUuid, Integer threadId, Integer mailId, Integer parsedMailId) {
        this.tenantId = tenantId;
        this.dateImport = dateImport;
        this.tenantUuid = tenantUuid;
        this.threadId = threadId;
        this.mailId = mailId;
        this.parsedMailId = parsedMailId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}