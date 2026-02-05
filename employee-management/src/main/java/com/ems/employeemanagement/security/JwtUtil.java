package com.ems.employeemanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
	private final String SECRET = "my_super_secret_key_my_super_secret_key_12345";

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(String email, Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) 
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(secretKey)
                .compact();
    }


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {

        Object id = extractAllClaims(token).get("id");

        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        }

        if (id instanceof Long) {
            return (Long) id;
        }

        throw new RuntimeException("Invalid ID type inside JWT");
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()  // ✅ Fixed: use parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
