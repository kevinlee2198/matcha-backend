package com.wilton.matcha.common.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtil {
    private static final String EMAIL_CLAIM = "email";
    private static final String FIRST_NAME_CLAIM = "given_name";
    private static final String LAST_NAME_CLAIM = "family_name";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";

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

    public static String getPrincipalFirstName(Jwt principal) {
        return principal.getClaimAsString(FIRST_NAME_CLAIM);
    }

    public static String getPrincipalLastName(Jwt principal) {
        return principal.getClaimAsString(LAST_NAME_CLAIM);
    }

    public static Set<String> getPrincipalRoles(Jwt principal) {
        Map<String, Object> resourceAccess = principal.getClaimAsMap(RESOURCE_ACCESS_CLAIM);

        return resourceAccess.values().stream()
                .filter(Objects::nonNull)
                .filter(client -> client instanceof Map)
                .map(client -> ((Map<?, ?>) client).get("roles"))
                .filter(roles -> roles instanceof List<?>)
                .flatMap(roles ->
                        ((List<?>) roles).stream().filter(Objects::nonNull).map(Object::toString))
                .collect(Collectors.toSet());
    }
}
