package com.amoalla.redis.command;

import com.amoalla.redis.exception.CommandParsingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SetCommandTest {

    static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("SET key value", new SetCommand("key", "value")),
                Arguments.of("SET key value NX", null),
                Arguments.of("SET key value XX", null),
                Arguments.of("SET key value GET", null),
                Arguments.of("SET key value PX 100", null),
                Arguments.of("SET key value EX 100", null),
                Arguments.of("SET key value PXAT 100", null),
                Arguments.of("SET key value NXAT 100", null),
                Arguments.of("SET key value KEEPTTL", null),
                Arguments.of("SET key value NX GET", null),
                Arguments.of("SET key value XX GET", null),
                Arguments.of("SET key value NX GET PX 100", null),
                Arguments.of("SET key value XX EX 100", null),
                Arguments.of("SET key value NX GET KEEPTTL", null),
                Arguments.of("SET key value KEEPTTL", null)
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
}