package com.amoalla.redis.command;

import com.amoalla.redis.types.Array;
import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;

import java.util.Iterator;
import java.util.List;

import static com.amoalla.redis.command.util.IteratorUtils.nextInt;
import static com.amoalla.redis.command.util.IteratorUtils.nextString;

public record PSyncCommand(String replicationId, int offset) implements RedisCommand {
    public static final String UNKNOWN_REPLICATION_ID = "?";

    public PSyncCommand() {
        this(UNKNOWN_REPLICATION_ID, -1);
    }

    public static PSyncCommand parse(List<Object> args) {
        Iterator<Object> iterator = args.iterator();
        String replicationId = nextString(iterator);
        int offset = nextInt(iterator);
        return new PSyncCommand(replicationId, offset);
    }

    @Override
    public DataType toDataType() {
        return new Array(List.of(new BulkString("PSYNC"),
                new BulkString(replicationId),
                new BulkString(Integer.toString(offset))));
    }
}
