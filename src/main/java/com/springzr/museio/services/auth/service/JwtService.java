package com.springzr.museio.services.auth.service;

/**
 * A port for generating JWT tokens.
 *
 * <p>The {@code JwtService} interface defines the contract for generating
 * JSON Web Tokens (JWT) for a user. This token can then be used for user authentication
 * in a secure manner.</p>
 */
public interface JwtService {

    /**
     * Generates a JWT token for the given user.
     *
     * <p>This method takes a User object and generates a corresponding JWT token
     * that can be used for authentication or authorization purposes.</p>
     *
     * @param accountId the user for whom the JWT token will be generated
     * @return a JWT token representing the authenticated user
     */
    String generateToken(Long accountId);

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username from the token
     */
    Long extractId(String token);

    /**
     * Validates the JWT token by checking the username and expiration.
     *
     * @param token the JWT token
     * @param accountId the user details to compare against the token
     * @return true if the token is valid, otherwise false
     */
    boolean isTokenValid(String token, Long accountId);
}