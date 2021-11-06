package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.errors.TokenRefreshException;
import com.keldkemp.applicationpanel.models.RefreshToken;
import com.keldkemp.applicationpanel.repositories.RefreshTokenRepository;
import com.keldkemp.applicationpanel.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableScheduling
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refreshTokenDurationSec}")
    private Long jwtRefreshTokenDurationSec;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserEntityRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusSeconds(jwtRefreshTokenDurationSec));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired");
        }

        return token;
    }

    @Transactional
    @Override
    public Integer deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 5000)
    @Transactional
    @Override
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
    }
}
