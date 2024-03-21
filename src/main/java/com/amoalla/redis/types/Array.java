package com.amoalla.redis.types;

import java.util.List;

public record Array(List<DataType> elements) implements DataType {
}
