package com.sp.telemedecine.services.emailService;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service
public class ConfirmationCodeGen {
    private SecureRandom random = new SecureRandom();

    public String generateConfirmationCode() {
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
