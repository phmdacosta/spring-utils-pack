package com.pedrocosta.utils.mailsender;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SimpleEmailSender implements Sender {

    private final JavaMailSender mailSender;
    private Email email;

    public SimpleEmailSender(JavaMailSender mailSender) {
        this(mailSender, null);
    }

    public SimpleEmailSender(JavaMailSender mailSender, Email email) {
        this.mailSender = mailSender;
        this.email = email;
    }

    public SimpleEmailSender from(String from) {
        if (this.getEmailObject() == null) {
            initEmail();
        }
        this.getEmailObject().setFrom(from);
        return this;
    }

    public SimpleEmailSender to(String ... to) {
        if (this.getEmailObject() == null) {
            initEmail();
        }
        this.getEmailObject().addTo(to);
        return this;
    }

    public SimpleEmailSender subject(String subject) {
        if (this.getEmailObject() == null) {
            initEmail();
        }
        this.getEmailObject().setSubject(subject);
        return this;
    }

    public SimpleEmailSender body(String body) {
        if (this.getEmailObject() == null) {
            initEmail();
        }
        this.getEmailObject().setBody(body);
        return this;
    }

    @Override
    public void send() {
        if (this.getEmailObject() != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(this.getEmailObject().getFrom());
            message.setTo(this.getEmailObject().getTo().toArray(new String[0]));
            message.setSubject(this.getEmailObject().getSubject());
            message.setText(this.getEmailObject().getBody());
            getMailSender().send(message);
        }
    }

    protected JavaMailSender getMailSender() {
        return this.mailSender;
    }

    protected Email getEmailObject() {
        return this.email;
    }

    private void initEmail() {
        this.email = new Email();
    }
}
