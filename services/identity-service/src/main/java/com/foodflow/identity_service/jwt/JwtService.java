package com.foodflow.identity_service.jwt;

import com.foodflow.identity_service.entity.User;
import com.foodflow.identity_service.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.refreshExpiry}")
    private String refreshExpiry;

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(Long.parseLong(expiration));

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user){

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(Long.parseLong(refreshExpiry));

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public Set<UserRole> getRoles(String token) {
        List<String> roles = extractClaims(token).get("roles", List.class);
        return roles.stream().map(UserRole::valueOf).collect(Collectors.toSet());
    }

}
