package com.plan.server.dto;

import lombok.Data;

@Data
public class OtpRequestDto {
    private String email;
    private String otp;
}