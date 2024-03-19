package com.amoalla.redis.handler;

import com.amoalla.redis.command.SetCommand;

public class SetHandler implements RedisCommandHandler<SetCommand> {
    @Override
    public Object handle(SetCommand command) {
        return null;
    }
}
