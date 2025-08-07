package com.wilton.matcha.common.util;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {
    private static final String EMAIL_CLAIM = "email";

    private SecurityUtil() {}

    public static Optional<Jwt> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Jwt) {
                return Optional.of((Jwt) principal);
            }
        }

        return Optional.empty();
    }

    public static String getPrincipalSubject(Jwt principal) {
        return principal.getSubject();
    }

    public static Optional<String> getPrincipalSubject() {
        return getPrincipal().map(SecurityUtil::getPrincipalSubject);
    }

    public static String getPrincipalEmail(Jwt principal) {
        return principal.getClaimAsString(EMAIL_CLAIM);
    }

    public static Optional<String> getPrincipalEmail() {
        return getPrincipal().map(SecurityUtil::getPrincipalEmail);
    }
}
