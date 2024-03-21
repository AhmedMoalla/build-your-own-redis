package com.amoalla.redis.handler;

import com.amoalla.redis.command.SetCommand;
import com.amoalla.redis.core.RedisCache;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.NullValue;
import com.amoalla.redis.types.SimpleString;
import lombok.RequiredArgsConstructor;

import java.util.List;

// https://redis.io/commands/set/
@RequiredArgsConstructor
public class SetHandler implements RedisCommandHandler<SetCommand> {

    private final RedisCache cache;

    @Override
    public List<DataType> handle(SetCommand command) {
        String key = command.key();

        if (command.mode().test(cache, key)) {
            putInCache(command);
        }

        if (command.setAndGet()) {
            String value = (String) cache.get(key);
            if (value == null) {
                return List.of(NullValue.INSTANCE);
            }
            return List.of(new BulkString(value));
        }

        return List.of(SimpleString.OK);
    }

    public void putInCache(SetCommand command) {
        if (!command.expiration().equals(SetCommand.INFINITE)) {
            cache.put(command.key(), command.value(), command.expiration());
        } else if (command.keepTTL()) {
            cache.putAndKeepTTL(command.key(), command.value());
        } else {
            cache.put(command.key(), command.value());
        }
    }
}
