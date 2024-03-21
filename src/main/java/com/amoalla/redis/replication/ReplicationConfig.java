package com.amoalla.redis.replication;

public record ReplicationConfig(String masterHost, int masterPort, boolean isSlave) {
}
