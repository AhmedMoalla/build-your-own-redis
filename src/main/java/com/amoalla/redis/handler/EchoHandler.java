package com.amoalla.redis.handler;

import com.amoalla.redis.command.EchoCommand;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;

public class EchoHandler implements RedisCommandHandler<EchoCommand> {
    @Override
    public DataType handle(EchoCommand command) {
        return new BulkString(command.input());
    }
}
