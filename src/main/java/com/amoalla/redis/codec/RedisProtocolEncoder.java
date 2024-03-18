package com.amoalla.redis.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.StandardCharsets;

public class RedisProtocolEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        String response = "";
        if (message instanceof String str) {
            response = "$" + str.length() + "\r\n" + str + "\r\n";
        }

        out.write(IoBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}