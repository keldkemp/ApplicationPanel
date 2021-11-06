package com.keldkemp.applicationpanel.config.jwt;

import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Log
public class JwtProvider {

    @Value("$(jwt.secret)")
    private String jwtSecret;

    @Value("${jwt.expirationSec}")
    private int jwtExpirationSec;

    public String generateToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusSeconds(jwtExpirationSec).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.severe("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.severe("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.severe("JWT token is expired: "+ e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.severe("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.severe("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
