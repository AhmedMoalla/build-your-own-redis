package com.amoalla.redis.codec;

import com.amoalla.redis.types.BulkString;
import com.amoalla.redis.types.DataType;
import com.amoalla.redis.types.NullValue;
import com.amoalla.redis.types.SimpleString;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.StandardCharsets;

public class RedisProtocolEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession session, Object raw, ProtocolEncoderOutput out) {
        if (!(raw instanceof DataType message)) {
            throw new UnsupportedOperationException(STR."Should not happen. Cannot encode \{raw.getClass().getName()}");
        }

        String response = switch (message) {
            case SimpleString string -> STR."+\{string.value()}\r\n";
            case BulkString string -> STR."$\{string.value().length()}\r\n\{string.value()}\r\n";
            case NullValue _ -> "$-1\r\n";
        };
        out.write(IoBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}