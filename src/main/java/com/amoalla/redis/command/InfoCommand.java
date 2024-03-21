package com.amoalla.redis.command;

import java.util.ArrayList;
import java.util.List;

public record InfoCommand(List<String> sections) implements RedisCommand {

    public static InfoCommand parse(List<Object> args) {
        List<String> sections = new ArrayList<>();
        args.forEach(arg -> sections.add((String) arg));
        return new InfoCommand(sections);
    }
}
