package com.heroku.java.SERVICES;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.heroku.java.MODEL.EventDetail;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set HTML content
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendHtmlEmailWithContentBuild(String to, String subject, EventDetail ed) {
        String htmlContent = buildHtmlContent(ed);
        sendHtmlEmail(to, subject, htmlContent);
    }

    private String buildHtmlContent(EventDetail ed) {
        StringBuilder message = new StringBuilder();
        message.append("<h2>Event Details</h2>");
        message.append("<p><strong>Event Name:</strong> ").append(ed.getEventname()).append("</p>");
        message.append("<p><strong>Event Type:</strong> ").append(ed.getEdtype()).append("</p>");
        message.append("<p><strong>Event Date:</strong> ").append(ed.getEddate()).append("</p>");
        return message.toString();
    }
}
