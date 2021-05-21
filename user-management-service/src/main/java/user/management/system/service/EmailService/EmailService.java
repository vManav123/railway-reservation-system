package user.management.system.service.emailService;

import javax.mail.MessagingException;

public interface EmailService {

    String sendSimpleEmail(String to, String body, String subject);

    String sendEmailWithAttachment(String to, String body, String subject) throws MessagingException;

}
