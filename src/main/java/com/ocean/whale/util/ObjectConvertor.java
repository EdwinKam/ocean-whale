package com.ocean.whale.util;

import com.ocean.whale.exception.WhaleException;
import com.ocean.whale.exception.WhaleServiceException;

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

    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance(); // Create a new instance of the class
        } catch (Exception e) {
            throw new WhaleServiceException(WhaleException.BAD_DATA_ERROR, "failed to construct instance of " + clazz.getName());
        }

        for (Field field : clazz.getDeclaredFields()) {
            Object value = map.get(field.getName()); // Get the value from the map
            if (value != null) {
                try {
                    field.setAccessible(true);
                    field.set(instance, value); // Set the field value
                } catch (IllegalAccessException e) {
                    throw new WhaleServiceException(WhaleException.BAD_DATA_ERROR, "failed to set field " + field.getName() + " in class" + clazz.getName());
                }
            }
        }

        return instance;
    }
}
