package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    Integer deleteByUserId(Long userId);

    void deleteExpiredTokens();
}
