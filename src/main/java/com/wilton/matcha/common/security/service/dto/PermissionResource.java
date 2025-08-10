package com.wilton.matcha.common.security.service.dto;

import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.StringUtils;

public class PermissionResource {
    private final String kind;
    private final String id;
    private final Map<String, PermissionAttribute> attributes;

    private PermissionResource(String kind, String id, Map<String, PermissionAttribute> attributes) {
        if (!StringUtils.hasText(kind)) {
            throw new IllegalStateException("kind cannot be empty");
        }

        this.kind = kind;
        this.id = id;
        this.attributes = MapUtils.emptyIfNull(attributes);
    }

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public Map<String, PermissionAttribute> getAttributes() {
        return attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String kind;
        private String id;
        private Map<String, PermissionAttribute> attributes;

        private Builder() {}

        public Builder kind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder attributes(Map<String, PermissionAttribute> attributes) {
            this.attributes = attributes;
            return this;
        }

        public PermissionResource build() {
            return new PermissionResource(kind, id, attributes);
        }
    }
}
