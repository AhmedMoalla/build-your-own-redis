package com.amoalla.redis.command;

public sealed interface RedisCommand permits EchoCommand, PingCommand, SetCommand {
}
