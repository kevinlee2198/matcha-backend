package com.wilton.matcha.common.util;

import java.util.stream.Stream;
import org.springframework.util.Assert;

public final class EnumUtil {
    private EnumUtil() {}

    /**
     * The same method as Enum#valueOf byt will match case-insensitively
     *
     * @param enumClass the final enum class
     * @param enumName  the string to be converted to the enum
     * @param <E>       the final enum class
     * @return the enum value from the string
     */
    public static <E extends Enum<E>> E valueOfIgnoreCase(final Class<E> enumClass, final String enumName) {
        Assert.hasLength(enumName, "enumName cannot be null or empty");
        Assert.isTrue(enumClass.isEnum(), "enum class " + enumName + " is not an enum");

        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> enumName.equalsIgnoreCase(e.name()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + enumClass));
    }
}
