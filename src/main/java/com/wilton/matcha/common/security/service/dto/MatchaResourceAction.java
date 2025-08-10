package com.wilton.matcha.common.security.service.dto;

import com.wilton.matcha.common.util.EnumUtil;
import java.util.Locale;

public enum MatchaResourceAction {
    READ,
    CREATE,
    UPDATE,
    DELETE;

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static MatchaResourceAction fromStringIgnoreCase(String enumName) {
        return EnumUtil.valueOfIgnoreCase(MatchaResourceAction.class, enumName);
    }
}
