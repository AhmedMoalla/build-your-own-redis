package com.amoalla.redis.replication;

import com.amoalla.redis.handler.info.InfoProvider;
import com.amoalla.redis.handler.info.InfoType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReplicationManager implements InfoProvider {

    private final ReplicationConfig config;

    private String role() {
        return config.isMaster() ? "master" : "slave";
    }

    @Override
    public String info() {
        return STR."# \{type().displayName()}\r\nrole:\{role()}\r\n";
    }

    @Override
    public InfoType type() {
        return InfoType.REPLICATION;
    }
}
