package com.amoalla.redis.types;

public record RDBFileSync(byte[] rdbFileBytes) implements DataType {
}
