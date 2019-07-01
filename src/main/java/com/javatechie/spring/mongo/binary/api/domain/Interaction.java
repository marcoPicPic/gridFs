package com.javatechie.spring.mongo.binary.api.domain;

import java.util.Date;

public class Interaction {

    private Integer id;
    private String mailSubject;
    private Date mailDate;
    private String mailBody;
    private Integer mailId;
    private Integer parsedMailId;
    private Integer tenantId;
    private Integer threadId;
    private Integer threadPriority;
    private Integer threadDesktop;
    private Integer threadKnowgroupId;
    private Integer threadLanguageId;
    private Integer threadQueueId;
    private Date indexftModificationDate;
    private String customerEmail;
    private Integer operatorId;
    private String criteriaIds;
    private String notes;
    private Integer vrExist;
    private String vrOperatorIds;
    private String vrRequesterIds;
    private String uuid;


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
}
