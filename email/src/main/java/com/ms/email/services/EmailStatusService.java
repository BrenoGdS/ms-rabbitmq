package com.ms.email.services;

import com.ms.email.model.EmailModel;
import com.ms.email.repositories.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailStatusService {

    final EmailRepository emailRepository;

    @Autowired
    public EmailStatusService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveEmailStatus(EmailModel emailModel) {
        emailRepository.save(emailModel);
    }
}
