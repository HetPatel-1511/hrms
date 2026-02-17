package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.EmailDetailsDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendSimpleMail(EmailDetailsDTO details)
    {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    public String
    sendMailWithAttachment(EmailDetailsDTO details)
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            
            if (details.getRecipients() != null && !details.getRecipients().isEmpty()) {
                mimeMessageHelper.setTo(details.getRecipients().toArray(new String[0]));
            } else {
                mimeMessageHelper.setTo(details.getRecipient());
            }
            
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            Resource resource = new UrlResource(details.getAttachment());

            mimeMessageHelper.addAttachment(resource.getFilename(), resource);

            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        catch (MessagingException e) {
            return "Error while sending mail!!!";
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
