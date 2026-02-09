package com.example.hrmsbackend.services;

import com.example.hrmsbackend.entities.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access_token_expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh_token_expiration}")
    private long refreshTokenExpiration;

    public String generateToken(Employee employee, String token_type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", employee.getId());
        claims.put("email", employee.getEmail());
        claims.put("roles", employee.getEmployeeRoleNames());
        return createToken(claims, employee.getEmail(), token_type);
    }

    private String createToken(Map<String, Object> claims, String username, String token_type) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(token_type=="REFRESH_TOKEN" ? new Date(System.currentTimeMillis() + refreshTokenExpiration) : new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public Boolean isExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
