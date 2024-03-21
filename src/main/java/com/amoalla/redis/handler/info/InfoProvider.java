package com.amoalla.redis.handler.info;

public interface InfoProvider {
    String info();
    InfoType type();
}
