package com.amoalla.redis.handler;

import com.amoalla.redis.command.PingCommand;

public class PingHandler implements RedisCommandHandler<PingCommand> {
    @Override
    public Object handle(PingCommand command) {
        return "PONG";
    }
}
