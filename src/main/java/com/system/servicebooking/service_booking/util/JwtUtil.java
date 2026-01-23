package com.system.servicebooking.service_booking.util;

import com.system.servicebooking.service_booking.enums.UsersRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String userId, UsersRole role) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token){
        return getClaims(token).getSubject();
    }

    public String extractRole(String token){
        return getClaims(token).get("role",String.class);
    }

    public boolean validateToken(String token){
        try {
            getClaims(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();

    }

}
