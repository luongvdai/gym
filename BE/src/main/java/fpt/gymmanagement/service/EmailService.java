package fpt.gymmanagement.service;

import fpt.gymmanagement.dto.request.EmailDetails;

public interface EmailService {
    void sendSimpleMail(EmailDetails details);

    void sendMailWithAttachment(EmailDetails details);
}
