package com.wilton.matcha.common.util;

import org.junit.jupiter.api.Test;

import static com.wilton.matcha.common.util.DefaultUtil.defaultIntLessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultUtilTest {

    @Test
    public void testDefaultIntLessThan() {
        Integer defaultValue = 10;

        // null case
        assertEquals(defaultValue, defaultIntLessThan(null, defaultValue));
        // value is greater case
        assertEquals(defaultValue, defaultIntLessThan(100, defaultValue));
        // value is lower case
        assertEquals(5, defaultIntLessThan(5, defaultValue));
    }
}
