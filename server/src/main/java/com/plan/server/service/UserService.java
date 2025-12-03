package com.plan.server.service;

import com.plan.server.dto.UserSignupDto;
import com.plan.server.model.User;
import com.plan.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final Map<String, PendingSignup> signupPending = new ConcurrentHashMap<>();
    private final Map<String, OtpInfo> loginOtps = new ConcurrentHashMap<>();

    public void requestSignup(UserSignupDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        String otp = generateOtp();
        emailService.sendOtp(dto.getEmail(), otp);
        signupPending.put(dto.getEmail(), new PendingSignup(dto, otp, LocalDateTime.now().plusMinutes(5)));
    }

    public User completeSignupAndGetUser(String email, String otp) {
        PendingSignup pending = signupPending.get(email);
        if (pending == null || LocalDateTime.now().isAfter(pending.expiry) || !pending.otp.equals(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }

        // Create user
        User user = new User();
        user.setFirstName(pending.dto.getFirstName());
        user.setLastName(pending.dto.getLastName());
        user.setEmail(pending.dto.getEmail());
        user.setRole(pending.dto.getRole());
        userRepository.save(user);

        // Clean up
        signupPending.remove(email);

        return user; // Return the user so controller can generate JWT
    }

    public void requestLogin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        String otp = generateOtp();
        emailService.sendOtp(email, otp);
        loginOtps.put(email, new OtpInfo(otp, LocalDateTime.now().plusMinutes(5)));
    }

    public User verifyLogin(String email, String otp) {
        OtpInfo info = loginOtps.get(email);
        if (info == null || LocalDateTime.now().isAfter(info.expiry) || !info.otp.equals(otp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP");
        }
        loginOtps.remove(email);
        return userRepository.findByEmail(email).orElseThrow();
    }

    private String generateOtp() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private static class PendingSignup {
        UserSignupDto dto;
        String otp;
        LocalDateTime expiry;

        PendingSignup(UserSignupDto dto, String otp, LocalDateTime expiry) {
            this.dto = dto;
            this.otp = otp;
            this.expiry = expiry;
        }
    }

    private static class OtpInfo {
        String otp;
        LocalDateTime expiry;

        OtpInfo(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }
}