package railway.application.system.Service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service(value = "emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    private final String attachment = "C:\\Users\\MVERMA\\Railway-Reservation-System\\railway-application-system\\src\\main\\resources\\Data\\templates\\Ticket.txt";
    private final String attachment2 = "C:\\Users\\MVERMA\\Railway-Reservation-System\\railway-application-system\\src\\main\\resources\\Data\\templates\\train.gif";


    @Override
    public void sendSimpleEmail(String to, String body, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(email);
        mailMessage.setTo(to);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);
        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendEmailWithAttachment(String to, String body, String subject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, true);

        mailMessage.setFrom(email);
        mailMessage.setTo(to);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);
        FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
        mailMessage.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
        FileSystemResource fileSystemResource1 = new FileSystemResource(new File(attachment2));
        mailMessage.addAttachment(fileSystemResource1.getFilename(), fileSystemResource);
        javaMailSender.send(mimeMessage);
    }

}
