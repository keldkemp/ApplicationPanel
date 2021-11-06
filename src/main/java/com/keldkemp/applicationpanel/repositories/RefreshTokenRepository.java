package com.keldkemp.applicationpanel.repositories;

import com.keldkemp.applicationpanel.models.RefreshToken;
import com.keldkemp.applicationpanel.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    Integer deleteByUser(Users user);

    void deleteAllByExpiryDateBefore(Instant date);
}
