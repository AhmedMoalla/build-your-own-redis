package com.amoalla.redis.types;

public sealed interface DataType permits Array, BulkString, NullValue, RDBFileSync, SimpleString {
}
