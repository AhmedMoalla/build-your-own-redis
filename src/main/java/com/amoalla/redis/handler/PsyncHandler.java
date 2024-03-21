package com.amoalla.redis.handler;

import com.amoalla.redis.command.PSyncCommand;
import com.amoalla.redis.replication.ReplicationManager;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.SimpleString;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PsyncHandler implements RedisCommandHandler<PSyncCommand> {

    private final ReplicationManager replicationManager;

    @Override
    public DataType handle(PSyncCommand command) {
        return new SimpleString(STR."FULLRESYNC \{replicationManager.replicationId()} 0");
    }
}
