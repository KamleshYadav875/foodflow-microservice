package com.foodflow.identity_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private static final Duration OTP_COOLDOWN = Duration.ofSeconds(30);

    public String generateSecureOtp() {

        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public boolean validateOtp(String otpKey, String otp) {
        String cachedOtp = redisTemplate.opsForValue().get(otpKey);
        return otp.equals(cachedOtp);
    }

    public void consumeOtp(String otpKey) {
        redisTemplate.delete(otpKey);
    }

    public void setOtp(String key, String value){
        redisTemplate.opsForValue().set(key, value, OTP_TTL);
    }

    public void setCooldown(String key, String value){
        redisTemplate.opsForValue().set(key, value, OTP_COOLDOWN);
    }
}
