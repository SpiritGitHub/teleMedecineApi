package com.sp.telemedecine.services.emailService;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${email.username}")
    private String fromEmail;

    @Value("${email.sendgrid.api_key}")
    private String sendgridApiKey;

    public void sendConfirmationEmail(String toEmail, String confirmationCode) {
        String subject = "Confirmation Code";
        String body = "Your confirmation code is: " + confirmationCode;

        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to send confirmation email", ex);
        }
    }
}
