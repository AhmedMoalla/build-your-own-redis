package com.amoalla.redis.handler;

import com.amoalla.redis.command.ReplConfCommand;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.SimpleString;

import java.util.List;

public class ReplConfHandler implements RedisCommandHandler<ReplConfCommand> {
    @Override
    public List<DataType> handle(ReplConfCommand command) {
        return List.of(SimpleString.OK);
    }
}
