package com.amoalla.redis.handler;

import com.amoalla.redis.command.RedisCommand;

public interface RedisCommandHandler<T extends RedisCommand> {
    Object handle(T command);
}
