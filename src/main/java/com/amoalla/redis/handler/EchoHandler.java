package com.amoalla.redis.handler;

import com.amoalla.redis.command.EchoCommand;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;

import java.util.List;

public class EchoHandler implements RedisCommandHandler<EchoCommand> {
    @Override
    public List<DataType> handle(EchoCommand command) {
        return List.of(new BulkString(command.input()));
    }
}
