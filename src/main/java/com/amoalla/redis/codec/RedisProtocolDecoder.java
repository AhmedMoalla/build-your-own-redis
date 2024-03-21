package com.amoalla.redis.codec;

import com.amoalla.redis.command.RedisCommand;
import com.amoalla.redis.command.RedisCommandType;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RedisProtocolDecoder implements ProtocolDecoder {

    private static final CharsetDecoder utf8Decoder = StandardCharsets.UTF_8.newDecoder();

    @Override
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//        System.out.println("=========================");
//        System.out.println(in.duplicate().getString(utf8Decoder));
//        System.out.println("=========================");
        Object message = decodeMessage(in);
        if (message instanceof Object[] arr) {
            RedisCommandType type = RedisCommandType.valueOf(((String) arr[0]).toUpperCase());
            Object[] args = new Object[arr.length - 1];
            System.arraycopy(arr, 1, args, 0, args.length);
            RedisCommand command = type.parse(Arrays.asList(args));
            out.write(command);
        }
    }

    private Object decodeMessage(IoBuffer buffer) throws CharacterCodingException, ProtocolDecoderException {
        byte typeByte = buffer.get();
        return switch (typeByte) {
            case '+' -> decodeString(buffer);
            case ':' -> decodeInteger(buffer);
            case '$' -> decodeBulkString(buffer);
            case '*' -> decodeArray(buffer);
            case '#' -> decodeBoolean(buffer);
            case '_' -> {
                consumeCRLF(buffer);
                yield null;
            }
            case 0x0A -> null;
            default -> throw new ProtocolDecoderException("Unexpected value: " + Character.toString(typeByte));
        };
    }

    private boolean decodeBoolean(IoBuffer buffer) throws ProtocolDecoderException {
        byte b = buffer.get();
        boolean result;
        if (b == 't') {
            result = true;
        } else if (b == 'f') {
            result = false;
        } else {
            throw new ProtocolDecoderException("Cannot parse boolean for value: " + Character.toString(b));
        }
        consumeCRLF(buffer);
        return result;
    }

    private Object[] decodeArray(IoBuffer buffer) throws ProtocolDecoderException, CharacterCodingException {
        int nbElts = decodeInteger(buffer);
        if (nbElts == -1) {
            consumeCRLF(buffer);
            return null;
        }
        Object[] array = new Object[nbElts];
        for (int i = 0; i < nbElts; i++) {
            array[i] = decodeMessage(buffer);
        }
        return array;
    }

    private String decodeBulkString(IoBuffer buffer) throws ProtocolDecoderException, CharacterCodingException {
        int length = decodeInteger(buffer);
        if (length == -1) return null;
        if (length == 0) return "";
        String result = buffer.getString(length, utf8Decoder);
        consumeCRLF(buffer);
        return result;
    }

    private int decodeInteger(IoBuffer buffer) throws ProtocolDecoderException {
        ByteBuffer temp = ByteBuffer.allocate(Long.SIZE);
        byte current = buffer.get();
        if (current != '-' && current != '+' && !Character.isDigit(current)) {
            throw new ProtocolDecoderException("Could not parse integer. First byte must be a sign (+/-) or a digit.");
        }
        temp.put(current);
        while (Character.isDigit(current = buffer.get())) {
            temp.put(current);
        }
        byte[] bytes = new byte[temp.position()];
        temp.flip();
        temp.get(bytes);
        buffer.position(buffer.position() - 1); // Cancel out the last buffer.get() in the while loop
        consumeCRLF(buffer);
        return Integer.parseInt(new String(bytes));
    }

    private String decodeString(IoBuffer buffer) throws CharacterCodingException {
        int i, initial;
        i = initial = buffer.position();
        for (; i < buffer.limit(); i++) {
            if (buffer.get(i) == '\r') {
                break;
            }
        }
        String result = buffer.getString(i - initial, utf8Decoder);
        consumeCRLF(buffer);
        return result;
    }

    private void consumeCRLF(IoBuffer buffer) {
        buffer.get(); // \r
        buffer.get(); // \n
    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {

    }

    @Override
    public void dispose(IoSession session) throws Exception {

    }
}