package com.wilton.matcha.common.security.service.dto;

import dev.cerbos.sdk.builders.AttributeValue;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionAttribute {
    private final AttributeValue attributeValue;

    private PermissionAttribute(AttributeValue attributeValue) {
        this.attributeValue = attributeValue;
    }

    public static PermissionAttribute stringValue(String value) {
        return new PermissionAttribute(AttributeValue.stringValue(value));
    }

    public static PermissionAttribute doubleValue(double value) {
        return new PermissionAttribute(AttributeValue.doubleValue(value));
    }

    public static PermissionAttribute boolValue(boolean value) {
        return new PermissionAttribute(AttributeValue.boolValue(value));
    }

    public static PermissionAttribute listValue(List<PermissionAttribute> values) {
        List<AttributeValue> valueList =
                values.stream().map(PermissionAttribute::getAttributeValue).toList();
        return new PermissionAttribute(AttributeValue.listValue(valueList));
    }

    public static PermissionAttribute listValue(PermissionAttribute... values) {
        return listValue(List.of(values));
    }

    public static PermissionAttribute mapValue(Map<String, PermissionAttribute> values) {
        Map<String, AttributeValue> attributeValueMap = values.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> entry.getValue().getAttributeValue()));

        return new PermissionAttribute(AttributeValue.mapValue(attributeValueMap));
    }

    public AttributeValue getAttributeValue() {
        return attributeValue;
    }
}
