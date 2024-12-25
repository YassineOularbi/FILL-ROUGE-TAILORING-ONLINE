package com.notification_mailing_service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Cache<String, String> otpCache;

    public OtpService() {
        this.otpCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    public String generateOtp(String email) {
        String otp = RandomStringUtils.random(6, true, true);
        otpCache.put(email, otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String cachedOtp = otpCache.getIfPresent(email);
        if (cachedOtp != null && cachedOtp.equals(otp)) {
            otpCache.invalidate(email);
            return true;
        }
        return false;
    }

}
