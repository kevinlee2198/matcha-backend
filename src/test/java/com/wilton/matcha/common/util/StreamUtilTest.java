package com.wilton.matcha.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class StreamUtilTest {
    @Test
    public void testZip() {
        List<String> listOne = List.of("list_1_1", "list_1_2", "list_1_3");
        List<String> listTwo = List.of("list_2_1", "list_2_2", "list_2_3");

        List<String> expected = List.of("list_1_1 list_2_1", "list_1_2 list_2_2", "list_1_3 list_2_3");

        List<String> result = StreamUtil.zip(listOne.stream(), listTwo.stream())
                .map(pair -> pair.getFirst() + " " + pair.getSecond())
                .toList();
        assertEquals(expected, result);
    }
}
