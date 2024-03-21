package com.amoalla.redis.handler;

import com.amoalla.redis.command.PingCommand;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.SimpleString;

import java.util.List;

public class PingHandler implements RedisCommandHandler<PingCommand> {
    @Override
    public List<DataType> handle(PingCommand command) {
        return List.of(new SimpleString("PONG"));
    }
}
