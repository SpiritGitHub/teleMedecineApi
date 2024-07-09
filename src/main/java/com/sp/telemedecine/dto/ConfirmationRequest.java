package com.sp.telemedecine.dto;

import lombok.Data;

@Data
public class ConfirmationRequest {
    private Long userId;
    private String confirmationCode;

}
