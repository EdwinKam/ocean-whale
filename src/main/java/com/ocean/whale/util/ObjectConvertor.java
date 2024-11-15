package com.ocean.whale.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjectConvertor {
    public static Map<String, Object> toMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        // Use reflection to access fields
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                // Handle potential exceptions
                e.printStackTrace();
            }
        }
        return map;
    }
}
