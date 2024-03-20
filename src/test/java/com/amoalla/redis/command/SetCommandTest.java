package com.amoalla.redis.command;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetCommandTest {

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("SET key value", builder().build()),
                Arguments.of("SET key value NX", builder()
                        .mode(SetCommand.SetMode.SET_IF_NOT_EXISTS)
                        .build()
                ),
                Arguments.of("SET key value XX", builder()
                        .mode(SetCommand.SetMode.SET_IF_EXISTS)
                        .build()),
                Arguments.of("SET key value GET", builder()
                        .setAndGet(true)
                        .build()),
                Arguments.of("SET key value PX 100", builder()
                        .expiration(Duration.ofMillis(100))
                        .build()),
                Arguments.of("SET key value EX 100", builder()
                        .expiration(Duration.ofSeconds(100))
                        .build()),
                Arguments.of("SET key value KEEPTTL", builder()
                        .keepTTL(true)
                        .build()),
                Arguments.of("SET key value NX GET", builder()
                        .setAndGet(true)
                        .mode(SetCommand.SetMode.SET_IF_NOT_EXISTS)
                        .build()),
                Arguments.of("SET key value XX GET", builder()
                        .setAndGet(true)
                        .mode(SetCommand.SetMode.SET_IF_EXISTS)
                        .build()),
                Arguments.of("SET key value NX GET PX 100", builder()
                        .setAndGet(true)
                        .expiration(Duration.ofMillis(100))
                        .mode(SetCommand.SetMode.SET_IF_NOT_EXISTS)
                        .build()),
                Arguments.of("SET key value XX EX 100", builder()
                        .expiration(Duration.ofSeconds(100))
                        .mode(SetCommand.SetMode.SET_IF_EXISTS)
                        .build()),
                Arguments.of("SET key value NX GET KEEPTTL", builder()
                        .setAndGet(true)
                        .keepTTL(true)
                        .mode(SetCommand.SetMode.SET_IF_NOT_EXISTS)
                        .build()),
                Arguments.of("SET key value KEEPTTL", builder()
                        .keepTTL(true)
                        .build())
        ).map(args -> {
            String[] splitCmd = ((String) args.get()[0]).split(" ");
            String[] argsArr = new String[splitCmd.length - 1];
            System.arraycopy(splitCmd, 1, argsArr, 0, argsArr.length);
            return Arguments.of(Arrays.asList(argsArr), args.get()[1]);
        });
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void ShouldParseSimpleSet(List<Object> args, SetCommand expected) {
        assertEquals(expected, SetCommand.parse(args));
    }

    private static SetCommand.SetCommandBuilder builder() {
        return SetCommand.builder().key("key").value("value")
                .mode(SetCommand.SetMode.DEFAULT)
                .expiration(SetCommand.INFINITE);
    }
}