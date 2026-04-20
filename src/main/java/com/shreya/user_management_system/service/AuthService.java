package com.shreya.user_management_system.service;

import com.shreya.user_management_system.dto.LoginRequestDto;
import com.shreya.user_management_system.dto.RegisterRequestDto;
import com.shreya.user_management_system.entity.Role;
import com.shreya.user_management_system.entity.User;
import com.shreya.user_management_system.repository.RoleRepository;
import com.shreya.user_management_system.repository.UserRepository;
import com.shreya.user_management_system.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public String register(RegisterRequestDto registerRequestDto){
        // check if username exists
        if(userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("Username already exist");
        }

        // fetch role of user..
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found"));

        // create new user....
        User user = User.builder().username(registerRequestDto.getUsername()).password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .roles(Set.of(roleUser)).build();

        // save user...
        userRepository.save(user);
        return "User registered successfully.";
    }

    public String login(LoginRequestDto loginRequestDto){
        // fetch user
        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(()-> new RuntimeException("User not found."));

        // validate password
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        // generate JWT
        return jwtService.generateToken(user);
    }
}
