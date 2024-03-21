package com.amoalla.redis.replication;

import com.amoalla.redis.handler.info.InfoProvider;
import com.amoalla.redis.handler.info.InfoType;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;

@RequiredArgsConstructor
public class ReplicationManager implements InfoProvider {

    private final String replicationId = generateReplicationId(40);
    private final ReplicationConfig config;

    private String role() {
        return config.isMaster() ? "master" : "slave";
    }

    @Override
    public String info() {
        return STR."""
        # \{type().displayName()}\r
        role:\{role()}\r
        master_replid:\{replicationId}\r
        master_repl_offset:0""";
    }

    @Override
    public InfoType type() {
        return InfoType.REPLICATION;
    }


    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static String generateReplicationId(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }

        return builder.toString();
    }

}
