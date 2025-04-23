package br.edu.ifes.mestrado.emailAPI.model;

import java.util.Date;
import java.util.List;

public class Email {
    private String subject;
    private String sender;
    private Date date;
    private String body;
    private List<String> attachmentPaths;


    public Email(String subject, String sender, Date date, String body, List<String> attachmentPaths) {
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.body = body;
        this.attachmentPaths = attachmentPaths;
    }

    public Email(String subject, String sender, Date date, String body) {
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.body = body;
    }

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }
    public String getSubject() { return subject; }
    public String getSender() { return sender; }
    public Date getDate() { return date; }
    public String getBody() { return body; }
}
