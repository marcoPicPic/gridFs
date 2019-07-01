package com.javatechie.spring.mongo.binary.api.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "interaction_for_gridfs")
public class Interaction {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "mail_subject")
    private String mailSubject;

    @Column(name = "mail_date")
    private Date mailDate;

    @Column(name = "mail_body")
    private String mailBody;

    @Column(name = "mail_id")
    private Integer mailId;

    @Column(name = "parsed_mail_id")
    private Integer parsedMailId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column(name = "thread_id")
    private Integer threadId;

    @Column(name = "thread_priority")
    private Integer threadPriority;

    @Column(name = "thread_desktop")
    private Integer threadDesktop;

    @Column(name = "thread_knowgroup_id")
    private Integer threadKnowgroupId;

    @Column(name = "thread_language_id")
    private Integer threadLanguageId;

    @Column(name = "thread_queue_id")
    private Integer threadQueueId;

    @Column(name = "indexft_modification_date")
    private Date indexftModificationDate;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "criteria_ids")
    private String criteriaIds;

    @Column(name = "notes")
    private String notes;

    @Column(name = "vr_exist")
    private Integer vrExist;

    @Column(name = "vr_operator_ids")
    private String vrOperatorIds;

    @Column(name = "vr_requester_ids")
    private String vrRequesterIds;

    @Column(name = "uuid_id")
    private String uuid;

    public Interaction() {
    }

    public Interaction(Integer id, String mailSubject, Date mailDate, String mailBody,
                       Integer mailId, Integer parsedMailId, Integer tenantId, Integer threadId,
                       Integer threadPriority, Integer threadDesktop, Integer threadKnowgroupId,
                       Integer threadLanguageId, Integer threadQueueId, Date indexftModificationDate,
                       String customerEmail, Integer operatorId, String criteriaIds, String notes,
                       Integer vrExist, String vrOperatorIds, String vrRequesterIds, String uuid) {
        this.id = id;
        this.mailSubject = mailSubject;
        this.mailDate = mailDate;
        this.mailBody = mailBody;
        this.mailId = mailId;
        this.parsedMailId = parsedMailId;
        this.tenantId = tenantId;
        this.threadId = threadId;
        this.threadPriority = threadPriority;
        this.threadDesktop = threadDesktop;
        this.threadKnowgroupId = threadKnowgroupId;
        this.threadLanguageId = threadLanguageId;
        this.threadQueueId = threadQueueId;
        this.indexftModificationDate = indexftModificationDate;
        this.customerEmail = customerEmail;
        this.operatorId = operatorId;
        this.criteriaIds = criteriaIds;
        this.notes = notes;
        this.vrExist = vrExist;
        this.vrOperatorIds = vrOperatorIds;
        this.vrRequesterIds = vrRequesterIds;
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public Date getMailDate() {
        return mailDate;
    }

    public void setMailDate(Date mailDate) {
        this.mailDate = mailDate;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public Integer getThreadPriority() {
        return threadPriority;
    }

    public void setThreadPriority(Integer threadPriority) {
        this.threadPriority = threadPriority;
    }

    public Integer getThreadDesktop() {
        return threadDesktop;
    }

    public void setThreadDesktop(Integer threadDesktop) {
        this.threadDesktop = threadDesktop;
    }

    public Integer getThreadKnowgroupId() {
        return threadKnowgroupId;
    }

    public void setThreadKnowgroupId(Integer threadKnowgroupId) {
        this.threadKnowgroupId = threadKnowgroupId;
    }

    public Integer getThreadLanguageId() {
        return threadLanguageId;
    }

    public void setThreadLanguageId(Integer threadLanguageId) {
        this.threadLanguageId = threadLanguageId;
    }

    public Integer getThreadQueueId() {
        return threadQueueId;
    }

    public void setThreadQueueId(Integer threadQueueId) {
        this.threadQueueId = threadQueueId;
    }

    public Date getIndexftModificationDate() {
        return indexftModificationDate;
    }

    public void setIndexftModificationDate(Date indexftModificationDate) {
        this.indexftModificationDate = indexftModificationDate;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getCriteriaIds() {
        return criteriaIds;
    }

    public void setCriteriaIds(String criteriaIds) {
        this.criteriaIds = criteriaIds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getVrExist() {
        return vrExist;
    }

    public void setVrExist(Integer vrExist) {
        this.vrExist = vrExist;
    }

    public String getVrOperatorIds() {
        return vrOperatorIds;
    }

    public void setVrOperatorIds(String vrOperatorIds) {
        this.vrOperatorIds = vrOperatorIds;
    }

    public String getVrRequesterIds() {
        return vrRequesterIds;
    }

    public void setVrRequesterIds(String vrRequesterIds) {
        this.vrRequesterIds = vrRequesterIds;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
