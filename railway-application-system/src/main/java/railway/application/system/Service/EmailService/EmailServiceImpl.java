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
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${railway.image}")
    private String attachment;

    public void sendSimpleEmail(String to,String body,String subject)
    {
        SimpleMailMessage mailMessage =new SimpleMailMessage();

        mailMessage.setFrom(email);
        mailMessage.setTo(to);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);
        javaMailSender.send(mailMessage);
    }

    public void sendEmailWithAttachment(String to,String body,String subject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage,true);

        mailMessage.setFrom(email);
        mailMessage.setTo(to);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);
        FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
        mailMessage.addAttachment(fileSystemResource.getFilename(),fileSystemResource);
        javaMailSender.send(mimeMessage);
    }

}
