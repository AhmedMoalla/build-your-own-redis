package com.amoalla.redis.command;

import com.amoalla.redis.types.Array;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;

import java.util.Iterator;
import java.util.List;

import static com.amoalla.redis.command.util.IteratorUtils.nextString;

public record ReplConfCommand(String key, String value) implements RedisCommand {

    public static ReplConfCommand parse(List<Object> args) {
        Iterator<Object> iterator = args.iterator();
        String key = nextString(iterator);
        String value = nextString(iterator);
        return new ReplConfCommand(key, value);
    }

    @Override
    public DataType toDataType() {
        return new Array(List.of(new BulkString("REPLCONF"), new BulkString(key), new BulkString(value)));
    }
}
