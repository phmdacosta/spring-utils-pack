package com.pedrocosta.utils.mailsender;

import com.pedrocosta.springutils.output.Log;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MimeEmailSender extends SimpleEmailSender {

    public MimeEmailSender(JavaMailSender mailSender) {
        super(mailSender);
    }

    public MimeEmailSender(JavaMailSender mailSender, Email email) {
        super(mailSender, email);
    }

    @Override
    public void send() {
        if (this.getEmailObject() != null) {
            try {
                MimeMessage message = getMailSender().createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
                helper.setFrom(this.getEmailObject().getFrom());
                helper.setTo(this.getEmailObject().getTo().toArray(new String[0]));
                helper.setSubject(this.getEmailObject().getSubject());
                helper.setText(this.getEmailObject().getBody());
            } catch (MessagingException e) {
                Log.error(this, e);
            }
        }
    }
}
