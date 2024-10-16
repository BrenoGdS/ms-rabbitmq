package com.ms.email.services;

import com.ms.email.enums.StatusEmailEnum;
import com.ms.email.model.EmailModel;
import com.ms.email.repositories.EmailRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    final EmailRepository emailRepository;
    final JavaMailSender emailSender;
    final String emailFrom;
    final EmailStatusService emailStatusService;

    public EmailService(EmailRepository emailRepository, JavaMailSender emailSender,
                        @Value("${spring.mail.username}") String emailFrom,
                        EmailStatusService emailStatusService) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
        this.emailFrom = emailFrom;
        this.emailStatusService = emailStatusService;
    }

    @Transactional
    public void sendEmail(EmailModel emailModel){
        try{
            emailModel.setEmailFrom(emailFrom);
            emailSender.send(getSimpleMailMessage(emailModel));
            emailModel.setStatusEmail(StatusEmailEnum.SENT);
        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmailEnum.ERROR);
        } finally {
            emailStatusService.saveEmailStatus(emailModel);
        }
    }

    private static SimpleMailMessage getSimpleMailMessage(EmailModel emailModel) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailModel.getEmailTo());
        message.setSubject(emailModel.getSubject());
        message.setText(emailModel.getText());
        return message;
    }
}
