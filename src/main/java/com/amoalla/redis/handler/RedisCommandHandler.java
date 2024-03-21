package com.amoalla.redis.handler;

import com.amoalla.redis.command.RedisCommand;
import com.amoalla.redis.types.DataType;

import java.util.List;

public interface RedisCommandHandler<T extends RedisCommand> {
    List<DataType> handle(T command);
}
