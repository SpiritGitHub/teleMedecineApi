package com.sp.telemedecine.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String userId;
    private String confirmationCode;
    private String newPassword;
}
