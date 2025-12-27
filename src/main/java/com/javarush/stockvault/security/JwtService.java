package com.javarush.stockvault.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtService {

    private static final String SECRET = "very-secret-key-very-secret-key-123456";
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
