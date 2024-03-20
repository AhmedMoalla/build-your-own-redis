package com.amoalla.redis.types;

public sealed interface DataType permits BulkString, NullValue, SimpleString {
}
