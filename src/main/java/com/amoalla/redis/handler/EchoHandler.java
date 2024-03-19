package com.amoalla.redis.handler;

import com.amoalla.redis.command.EchoCommand;

public class EchoHandler implements RedisCommandHandler<EchoCommand> {
    @Override
    public Object handle(EchoCommand command) {
        return command.input();
    }
}
