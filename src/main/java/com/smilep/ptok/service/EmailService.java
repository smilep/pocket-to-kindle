package com.smilep.ptok.service;

import com.smilep.ptok.config.MailProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.nio.file.Path;
import java.util.Properties;

// TODO - switch to spring boot starter mail
@Slf4j
@Service
public class EmailService {

    private MailProps mailProps;

    public EmailService(MailProps mailProps) {
        this.mailProps = mailProps;
    }

    public void send(Path path) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", mailProps.getHost());
        properties.put("mail.smtp.port", mailProps.getPort());
        properties.put("mail.smtp.ssl.enable", mailProps.isSslenable());
        properties.put("mail.smtp.auth", mailProps.isAuth());
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProps.getFrom(), mailProps.getPassword());
            }
        });
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(mailProps.getFrom());
            message.addRecipients(Message.RecipientType.TO, mailProps.getTo());
            message.setSubject("convert");
            message.setText("");
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("",e);
            throw new RuntimeException("Email Error", e);
        }
    }
}
