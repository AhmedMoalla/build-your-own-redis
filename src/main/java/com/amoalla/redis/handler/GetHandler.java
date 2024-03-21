package com.amoalla.redis.handler;

import com.amoalla.redis.command.GetCommand;
import com.amoalla.redis.core.RedisCache;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.NullValue;
import com.amoalla.redis.types.SimpleString;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetHandler implements RedisCommandHandler<GetCommand> {

    private final RedisCache cache;

    @Override
    public List<DataType> handle(GetCommand command) {
        Object value = cache.get(command.key());
        if (value instanceof String string) {
            return List.of(new SimpleString(string));
        }

        if (value == null) {
            return List.of(NullValue.INSTANCE);
        }

        throw new IllegalArgumentException("GET can only handle string values");
    }
}
