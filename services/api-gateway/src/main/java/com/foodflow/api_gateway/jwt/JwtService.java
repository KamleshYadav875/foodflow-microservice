package com.foodflow.api_gateway.jwt;

import com.foodflow.api_gateway.enums.UserRole;
import com.foodflow.api_gateway.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (ExpiredJwtException ex) {
            throw new UnauthorizedException("Token expired");
        }
        catch (JwtException ex) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public Long getUserId(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }

    public List<UserRole> getRoles(String token) {
        List<String> roleNames = extractClaims(token).get("roles", List.class);

        return roleNames.stream()
                .map(UserRole::valueOf)
                .toList();
    }
}
