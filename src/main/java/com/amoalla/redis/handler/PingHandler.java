package com.amoalla.redis.handler;

import com.amoalla.redis.command.PingCommand;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.SimpleString;

public class PingHandler implements RedisCommandHandler<PingCommand> {
    @Override
    public DataType handle(PingCommand command) {
        return new SimpleString("PONG");
    }
}
