package com.amoalla.redis.codec;

import com.amoalla.redis.command.RedisCommand;
import com.amoalla.redis.types.*;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RedisProtocolEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession session, Object raw, ProtocolEncoderOutput out) {
        if (raw instanceof RedisCommand command) {
            raw = command.toDataType();
        }
        if (!(raw instanceof DataType message)) {
            throw new UnsupportedOperationException(STR."Should not happen. Cannot encode \{raw.getClass().getName()}");
        }

        String response = encode(message);
        out.write(IoBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
    }

    private String encode(DataType message) {
        return switch (message) {
            case SimpleString string -> STR."+\{string.value()}\r\n";
            case BulkString string -> STR."$\{string.value().length()}\r\n\{string.value()}\r\n";
            case NullValue _ -> "$-1\r\n";
            case Array arr -> STR."*\{arr.elements().size()}\r\n" + arr.elements().stream().map(this::encode).collect(Collectors.joining());
        };
    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}