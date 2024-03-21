package com.amoalla.redis.command;

import com.amoalla.redis.types.DataType;

public sealed interface RedisCommand permits EchoCommand, GetCommand, InfoCommand, PingCommand, ReplConfCommand, SetCommand {
    default DataType toDataType() {
        return null;
    }
}
