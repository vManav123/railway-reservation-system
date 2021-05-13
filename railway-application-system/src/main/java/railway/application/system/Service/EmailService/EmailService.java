package railway.application.system.Service.EmailService;

import javax.mail.MessagingException;

public interface EmailService {

    void sendSimpleEmail(String to, String body, String subject);

    void sendEmailWithAttachment(String to, String body, String subject) throws MessagingException;

}
