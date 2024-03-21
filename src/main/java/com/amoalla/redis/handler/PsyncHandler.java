package com.amoalla.redis.handler;

import com.amoalla.redis.command.PSyncCommand;
import com.amoalla.redis.replication.ReplicationManager;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.RDBFileSync;
import com.amoalla.redis.types.SimpleString;
import lombok.RequiredArgsConstructor;

import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
public class PsyncHandler implements RedisCommandHandler<PSyncCommand> {

    private final ReplicationManager replicationManager;

    @Override
    public List<DataType> handle(PSyncCommand command) {
        try {
            byte[] bytes = Base64.getDecoder().decode("UkVESVMwMDEx+glyZWRpcy12ZXIFNy4yLjD6CnJlZGlzLWJpdHPAQPoFY3RpbWXCbQi8ZfoIdXNlZC1tZW3CsMQQAPoIYW9mLWJhc2XAAP/wbjv+wP9aog==");
            return List.of(
                    new SimpleString(STR."FULLRESYNC \{replicationManager.replicationId()} 0"),
                    new RDBFileSync(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
