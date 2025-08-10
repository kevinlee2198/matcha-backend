package com.wilton.matcha.common.security.service.dto;

import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.util.StringUtils;

public class PermissionPrincipal {
    private final String id;
    private final Set<String> roles;
    private final Map<String, PermissionAttribute> attributes;

    private PermissionPrincipal(String id, Set<String> roles, Map<String, PermissionAttribute> attributes) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalStateException("principal cannot be empty");
        }

        this.id = id;
        this.roles = SetUtils.emptyIfNull(roles);
        this.attributes = MapUtils.emptyIfNull(attributes);
    }

    public String getId() {
        return id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Map<String, PermissionAttribute> getAttributes() {
        return attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private Set<String> roles;
        private Map<String, PermissionAttribute> attributes;

        private Builder() {}

        public Builder id(String principal) {
            this.id = principal;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder attributes(Map<String, PermissionAttribute> attributes) {
            this.attributes = attributes;
            return this;
        }

        public PermissionPrincipal build() {
            return new PermissionPrincipal(id, roles, attributes);
        }
    }
}
