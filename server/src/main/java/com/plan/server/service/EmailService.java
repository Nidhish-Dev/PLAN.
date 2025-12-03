package com.plan.server.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Your PLAN Verification Code");

            String htmlBody = getOtpEmailTemplate(otp);

            helper.setText(htmlBody, true); // true = HTML
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }

    private String getOtpEmailTemplate(String otp) {
        // Fixed: Use %s with String.format() instead of .formatted() to avoid Java 21 printf bug
        return String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Your PLAN OTP</title>
                <style>
                    body { margin:0; padding:0; background:#000; font-family:'Segoe UI',sans-serif; color:#fff; }
                    .container { max-width:600px; margin:40px auto; background:linear-gradient(135deg,#0f0f0f,#1a1a1a); border-radius:16px; overflow:hidden; box-shadow:0 20px 40px rgba(0,0,0,0.6); border:1px solid #333; }
                    .header { background:linear-gradient(90deg,#00ff88,#00cc66); padding:40px 30px; text-align:center; }
                    .logo { font-size:42px; font-weight:900; letter-spacing:-2px; color:#000; text-shadow:2px 2px 4px rgba(0,0,0,0.3); }
                    .tagline { font-size:14px; color:#000; font-weight:600; margin-top:8px; opacity:0.9; }
                    .content { padding:40px 30px; text-align:center; }
                    .title { font-size:28px; font-weight:700; margin-bottom:16px; background:linear-gradient(90deg,#00ff88,#00cc66); -webkit-background-clip:text; -webkit-text-fill-color:transparent; background-clip:text; }
                    .message { font-size:18px; line-height:1.6; color:#e0e0e0; margin-bottom:30px; }
                    .otp-box { display:inline-block; background:#00ff88; color:#000; font-size:36px; font-weight:900; letter-spacing:8px; padding:20px 40px; border-radius:12px; margin:30px 0; box-shadow:0 10px 30px rgba(0,255,136,0.3); }
                    .info { font-size:14px; color:#888; line-height:1.7; margin-top:40px; padding:20px; background:#111; border-radius:10px; border-left:4px solid #00ff88; }
                    .footer { text-align:center; padding:30px; background:#0a0a0a; color:#555; font-size:12px; }
                    .highlight { color:#00ff88; font-weight:600; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">PLAN.</div>
                        <div class="tagline">Your Smart Project Manager</div>
                    </div>
                    <div class="content">
                        <h1 class="title">Your Verification Code</h1>
                        <p class="message">
                            Welcome to <strong>PLAN.</strong><br>
                            Here's your one-time verification code:
                        </p>
                        <div class="otp-box">%s</div>
                        <div class="info">
                            This code expires in <span class="highlight">5 minutes</span>.<br>
                            Never share your OTP with anyone.
                        </div>
                    </div>
                    <div class="footer">
                        © 2025 PLAN. • Made with ❤️ for focused builders
                    </div>
                </div>
            </body>
            </html>
            """, otp); // ← This is safe and works perfectly on Java 21
    }
}