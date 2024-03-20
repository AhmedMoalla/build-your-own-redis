package com.amoalla.redis.handler;

import com.amoalla.redis.command.RedisCommand;
import com.amoalla.redis.types.DataType;

public interface RedisCommandHandler<T extends RedisCommand> {
    DataType handle(T command);
}
