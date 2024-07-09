package com.sp.telemedecine.services.emailService;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConfirmationCodeRepo {
    private Map<Long, String> confirmationCodes = new HashMap<>();

    public void storeConfirmationCode(Long userId, String confirmationCode) {
        confirmationCodes.put(userId, confirmationCode);
    }

    public String retrieveConfirmationCode(Long userId) {
        return confirmationCodes.get(userId);
    }

    public void removeConfirmationCode(Long userId) {
        confirmationCodes.remove(userId);
    }
}
