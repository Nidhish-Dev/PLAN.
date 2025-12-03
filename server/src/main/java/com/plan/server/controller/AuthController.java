package com.plan.server.controller;

import com.plan.server.dto.OtpRequestDto;
import com.plan.server.dto.UserSignupDto;
import com.plan.server.model.User;
import com.plan.server.service.JwtService;
import com.plan.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/request-signup")
    public ResponseEntity<String> requestSignup(@RequestBody UserSignupDto dto) {
        userService.requestSignup(dto);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-signup")
    public ResponseEntity<String> verifySignup(@RequestBody OtpRequestDto dto) {
        User user = userService.completeSignupAndGetUser(dto.getEmail(), dto.getOtp());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);  // Now returns JWT instead of plain message
    }

    @PostMapping("/request-login")
    public ResponseEntity<String> requestLogin(@RequestBody OtpRequestDto dto) { // Using OtpRequestDto but only email needed
        userService.requestLogin(dto.getEmail());
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-login")
    public ResponseEntity<String> verifyLogin(@RequestBody OtpRequestDto dto) {
        User user = userService.verifyLogin(dto.getEmail(), dto.getOtp());
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(token);
    }
}