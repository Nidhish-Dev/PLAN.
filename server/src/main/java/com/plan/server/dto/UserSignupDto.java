package com.plan.server.dto;

import com.plan.server.enums.Role;
import lombok.Data;

@Data
public class UserSignupDto {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}