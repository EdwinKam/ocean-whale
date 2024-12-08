package com.ocean.whale.util;

import java.util.UUID;

public class IdGenerator {
    public static String length(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
