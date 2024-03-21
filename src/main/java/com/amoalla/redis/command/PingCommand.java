package com.amoalla.redis.command;

import com.amoalla.redis.types.Array;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;

import java.util.List;

public final class PingCommand implements RedisCommand {
    private static final PingCommand INSTANCE = new PingCommand();

    public static PingCommand parse(List<Object> args) {
        return INSTANCE;
    }

    @Override
    public DataType toDataType() {
        return new Array(List.of(new BulkString("ping")));
    }
}
