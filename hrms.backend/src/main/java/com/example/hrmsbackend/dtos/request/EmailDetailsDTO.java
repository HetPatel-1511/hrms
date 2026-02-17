package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class EmailDetailsDTO {
    private String recipient;
    private List<String> recipients;

    @NotNull
    private String msgBody;

    @NotNull
    private String subject;

    @NotNull
    private String attachment;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
