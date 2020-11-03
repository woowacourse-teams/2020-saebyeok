package com.saebyeok.saebyeok.infra;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private String secretKey;
    private final UserDetailsService userDetailsService;
    private long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey, @Value("${security.jwt.token.expire-length}") long validityInMilliseconds, @Qualifier("userService") UserDetailsService userDetailsService) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String subject) {
        Claims claims = Jwts.claims().setSubject(subject);

        Date now = new Date();
        Date validity = new Date(now.getTime()
                + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getSubject(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

