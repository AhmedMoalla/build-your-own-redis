package com.amoalla.redis.handler.info;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InfoType {
    SERVER("Server"),
    CLIENTS("Clients"),
    MEMORY("Memory"),
    PERSISTENCE("Persistence"),
    STATS("Stats"),
    REPLICATION("Replication"),
    CPU("CPU"),
    MODULES("Modules"),
    ERRORSTATS("Errorstats"),
    CLUSTER("Cluster"),
    KEYSPACE("Keyspace");

    private final String displayName;


    public String displayName() {
        return displayName;
    }
}
