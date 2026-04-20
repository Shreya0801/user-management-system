package com.shreya.user_management_system.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
