package springboot.belzedev.it.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String token){
        try{
            String url = "https://junction.proxy.rlwy.net/api/verify-email?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("zararaz2002@gmail.com");
            message.setTo(to);
            message.setSubject("Emeil Verification");
            message.setText("Please verify your email by clicking the link below:\n" + url);
            mailSender.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
