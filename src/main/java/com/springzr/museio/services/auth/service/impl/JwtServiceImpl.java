package com.springzr.museio.services.auth.service.impl;

import com.springzr.museio.services.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for handling JWT operations such as generating, validating, and extracting claims
 * from JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${app.jwt.secret}")
    String secretKey;

    @Value("${app.jwt.token-expiration}")
    Integer tokenExpiration;

    @Override
    public String generateToken(Long accountId) {
        return Jwts
                .builder()
                .subject(accountId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    @Override
    public Long extractId(String token) {
        String accountId = extractClaim(token, Claims::getSubject);
        return Long.valueOf(accountId);
    }

    @Override
    public boolean isTokenValid(String token, Long accountId) {
        final Long id = extractId(token);
        return Objects.equals(id, accountId) && !isTokenExpired(token);
    }

    /**
     * Checks whether the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, otherwise false
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver a function that extracts the claim from the JWT claims
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves the signing key used for signing the JWT token.
     *
     * @return the signing key
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return the claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .clockSkewSeconds(5)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
