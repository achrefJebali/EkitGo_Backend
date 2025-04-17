package tn.esprit.examenspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendPurchaseConfirmation(String to, String studentName, String formationName) {
        String subject = "Formation Purchase Confirmation";
        String text = String.format("Hi %s,\n\nYou have purchased the formation: %s.\n\nThank you for your purchase!\n\nBest regards,\nElitGo Team", studentName, formationName);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
