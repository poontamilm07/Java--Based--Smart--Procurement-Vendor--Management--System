package com.procurement.procurement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 🔐 Must be at least 256 bits (32+ chars) for HS256
    @Value("${jwt.secret}")
    private String jwtSecret;

    // ⏱ 7 days (604800000 ms)
    @Value("${jwt.expiration:604800000}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        if (jwtSecret.length() < 32) {
            throw new IllegalStateException(
                    "JWT secret key must be at least 32 characters long"
            );
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // ===================== Generate JWT =====================
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ===================== Extract Username =====================
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ===================== Validate Token =====================
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            System.err.println("Invalid JWT token: " + ex.getMessage());
        }
        return false;
    }
}
