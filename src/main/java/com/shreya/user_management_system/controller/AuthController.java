package com.shreya.user_management_system.controller;

import com.shreya.user_management_system.dto.LoginRequestDto;
import com.shreya.user_management_system.dto.RegisterRequestDto;
import com.shreya.user_management_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDto registerRequestDto){
        return authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public  String login(@RequestBody LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto);
    }
}
