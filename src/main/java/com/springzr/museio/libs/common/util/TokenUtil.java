package com.springzr.museio.libs.common.util;

import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Utility class for Token related functions.
 *
 */
public class TokenUtil {

    /**
     * Retrieved the id based on the token.
     *
     * @return the account id stored in the token
     */
    public static Long getAccountId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
