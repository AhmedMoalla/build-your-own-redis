package com.amoalla.redis.types;

public record SimpleString(String value) implements DataType {
    public static final SimpleString OK = new SimpleString("OK");
}
