package br.edu.ifes.mestrado.emailAPI.model;

import java.util.Date;

public class Email {
    private String subject;
    private String sender;
    private Date date;
    private String body;

    public Email(String subject, String sender, Date date, String body) {
        this.subject = subject;
        this.sender = sender;
        this.date = date;
        this.body = body;
    }

    public String getSubject() { return subject; }
    public String getSender() { return sender; }
    public Date getDate() { return date; }
    public String getBody() { return body; }
}
