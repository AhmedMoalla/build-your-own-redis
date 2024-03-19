package com.amoalla.redis.command.util;

import com.amoalla.redis.exception.CommandParsingException;

import java.util.Iterator;

public class IteratorUtils {
    public static String nextString(Iterator<Object> iterator) {
        return (String) iterator.next();
    }

    public static int nextInt(Iterator<Object> iterator) {
        return Integer.parseInt((String) iterator.next());
    }

    public static int nextPositiveInt(Iterator<Object> iterator) {
        int i = nextInt(iterator);
        if (i < 0) throw new CommandParsingException("Expected positive integer, got " + i + " instead.");
        return i;
    }
}
