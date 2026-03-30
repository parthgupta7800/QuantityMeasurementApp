package com.app.quantitymeasurement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY="mysecretkeymysecretkeymysecretkey"; 
    // must be at least 32 chars for HS256

    private static final long EXPIRATION_TIME=1000*60*60*24; // 1 day

    private final Key key=Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generate Token
    public String generateToken(String email){

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract User name (email)
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    // Extract Expiration
    public Date extractExpiration(String token){
        return extractClaims(token).getExpiration();
    }

    // Extract All Claims
    private Claims extractClaims(String token){

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate Token
    public boolean validateToken(String token,String email){

        String extractedEmail=extractUsername(token);

        return extractedEmail.equals(email)
                && !extractExpiration(token).before(new Date());
    }
}