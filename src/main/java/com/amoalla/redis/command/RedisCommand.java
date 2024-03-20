package com.amoalla.redis.command;

public sealed interface RedisCommand permits EchoCommand, GetCommand, PingCommand, SetCommand {
}
