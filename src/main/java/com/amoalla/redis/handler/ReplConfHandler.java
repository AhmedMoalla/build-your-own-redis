package com.amoalla.redis.handler;

import com.amoalla.redis.command.ReplConfCommand;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.SimpleString;

public class ReplConfHandler implements RedisCommandHandler<ReplConfCommand> {
    @Override
    public DataType handle(ReplConfCommand command) {
        return SimpleString.OK;
    }
}
