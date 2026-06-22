package com.ideasnap.service;

import com.ideasnap.domain.RefreshToken;
import com.ideasnap.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface TokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(UUID userId);
}
