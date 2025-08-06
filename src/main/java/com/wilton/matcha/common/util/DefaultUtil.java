package com.wilton.matcha.common.util;

import java.util.function.Function;

public final class DefaultUtil {

    private DefaultUtil() {
    }

    public static <T> T defaultValue(T value, T defaultValue, Function<T, Boolean> validationFunction) {
        if (value == null) {
            return defaultValue;
        } else if (!validationFunction.apply(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public static Integer defaultIntLessThan(Integer value, Integer defaultValue) {
        return defaultValue(value, defaultValue, (currentValue) -> currentValue < defaultValue);
    }

    public static Integer defaultIntLessOrEqualThan(Integer value, Integer defaultValue) {
        return defaultValue(value, defaultValue, (currentValue) -> currentValue <= defaultValue);
    }

    public static Integer defaultIntGreaterThan(Integer value, Integer defaultValue) {
        return defaultValue(value, defaultValue, (currentValue) -> currentValue > defaultValue);
    }

    public static Integer defaultIntGreaterOrEqualThan(Integer value, Integer defaultValue) {
        return defaultValue(value, defaultValue, (currentValue) -> currentValue >= defaultValue);
    }
}
