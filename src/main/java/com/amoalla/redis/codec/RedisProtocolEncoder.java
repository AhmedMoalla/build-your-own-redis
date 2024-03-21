package com.amoalla.redis.codec;

import com.amoalla.redis.command.RedisCommand;
import com.amoalla.redis.types.*;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class RedisProtocolEncoder implements ProtocolEncoder {

    private static final CharsetEncoder encoder = StandardCharsets.ISO_8859_1.newEncoder();

    @Override
    public void encode(IoSession session, Object raw, ProtocolEncoderOutput out) throws CharacterCodingException {
        if (raw instanceof RedisCommand command) {
            raw = command.toDataType();
        }
        if (!(raw instanceof DataType message)) {
            throw new UnsupportedOperationException(STR."Should not happen. Cannot encode \{raw.getClass().getName()}");
        }

        String response = encode(message);
        out.write(IoBuffer.wrap(encoder.encode(CharBuffer.wrap(response))));
    }

    private String encode(DataType message) {
        return switch (message) {
            case SimpleString(var value) -> STR."+\{value}\r\n";
            case BulkString(var value) -> STR."$\{value.length()}\r\n\{value}\r\n";
            case NullValue _ -> "$-1\r\n";
            case Array(var elements) -> STR."*\{elements.size()}\r\n" + elements.stream().map(this::encode).collect(Collectors.joining());
            case RDBFileSync(var bytes) -> STR."$\{bytes.length}\r\n\{new String(bytes)}";
        };
    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}