package com.unicyb.energytaxi.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailForgotPasswordService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;
    private String OPT;
    public void sendOTPCode(String toEmail) throws MessagingException {
        OPT = generateOTP();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(toEmail);
        String content = "<p>Hello from Energy Taxi</p>" + "<p>For security reason, you're required to use the following code</p>"
                + "<p><b>" + OPT + "</b></p>" + "<br>" + "<p>Note: this OPT is set to expire in 5 minutes.</p>";
        mimeMessageHelper.setText(content, true);
        mimeMessageHelper.setSubject("Here is your reset code - Expire in 5 minutes!");
        javaMailSender.send(mimeMessage);
        System.out.println("Email send....");
    }

    public String generateOTP(){
        return RandomStringUtils.random(8, true, true);
    }

    public String getOPT() {
        return OPT;
    }
}
