package com.amoalla.redis.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class RedisProtocolCodecFactory implements ProtocolCodecFactory {

    @Override
    public ProtocolEncoder getEncoder(IoSession session) {
        return new RedisProtocolEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) {
        return new RedisProtocolDecoder();
    }
}