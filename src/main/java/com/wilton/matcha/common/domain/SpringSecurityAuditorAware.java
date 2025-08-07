package com.wilton.matcha.common.domain;

import com.wilton.matcha.common.util.SecurityUtil;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtil.getPrincipalSubject().orElse("System"));
    }
}
