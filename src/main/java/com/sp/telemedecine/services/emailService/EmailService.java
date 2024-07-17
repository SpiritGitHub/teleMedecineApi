package com.sp.telemedecine.services.emailService;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.template.id}")
    private String templateId;

    public void sendTemplateEmail(String toEmail, String subject, Map<String, String> templateData) throws IOException {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.setSubject(subject); // Define the subject here

        for (Map.Entry<String, String> entry : templateData.entrySet()) {
            personalization.addDynamicTemplateData(entry.getKey(), entry.getValue());
        }

        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Email sent! Status code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            System.out.println("Response headers: " + response.getHeaders());

            if (response.getStatusCode() != 202) {
                // Log detailed error information
                System.err.println("Failed to send email. Status code: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
                System.err.println("Response headers: " + response.getHeaders());
            }
        } catch (IOException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
            throw ex;
        }
    }

    public void sendConfirmationEmail(String toEmail, String confirmationCode) throws IOException {
        String subject = "Your Confirmation Code";
        Map<String, String> templateData = new HashMap<>();
        templateData.put("confirmationCode", confirmationCode);
        sendTemplateEmail(toEmail, subject, templateData);
    }
}
