package com.amoalla.redis.replication;

import com.amoalla.redis.handler.info.InfoProvider;
import com.amoalla.redis.handler.info.InfoType;

public class ReplicationManager implements InfoProvider {

    @Override
    public String info() {
        return STR."# \{type().displayName()}\r\nrole:master\r\n";
    }

    @Override
    public InfoType type() {
        return InfoType.REPLICATION;
    }
}
