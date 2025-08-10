package com.wilton.matcha.common.security;

import com.wilton.matcha.common.domain.MatchaUser;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class MatchaUserJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = SecurityUtil.getPrincipalRoles(source).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());

        MatchaUser matchaUser = new MatchaUser(
                SecurityUtil.getPrincipalSubject(source),
                SecurityUtil.getPrincipalEmail(source),
                SecurityUtil.getPrincipalFirstName(source),
                SecurityUtil.getPrincipalLastName(source),
                authorities);
        // Create Authentication with your User as the principal
        return new JwtAuthenticationToken(source, authorities, matchaUser.id()) {
            @Override
            public Object getPrincipal() {
                return matchaUser;
            }
        };
    }
}
