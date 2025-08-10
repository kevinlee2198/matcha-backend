package com.wilton.matcha.common.domain;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public record MatchaUser(
        String id,
        String email,
        String firstName,
        String lastName,
        Collection<? extends GrantedAuthority> authorities) {}
