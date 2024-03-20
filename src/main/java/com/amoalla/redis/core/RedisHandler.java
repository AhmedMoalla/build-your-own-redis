package com.amoalla.redis.core;

import com.amoalla.redis.command.*;
import com.amoalla.redis.handler.EchoHandler;
import com.amoalla.redis.handler.GetHandler;
import com.amoalla.redis.handler.PingHandler;
import com.amoalla.redis.handler.SetHandler;
import lombok.RequiredArgsConstructor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

@RequiredArgsConstructor
public class RedisHandler extends IoHandlerAdapter {

    private final RedisCache cache;

    @Override
    public void messageReceived(IoSession session, Object message) {
        if (message instanceof RedisCommand command) {
            Object response = switch (command) {
                case EchoCommand cmd -> new EchoHandler().handle(cmd);
                case PingCommand cmd -> new PingHandler().handle(cmd);
                case SetCommand cmd -> new SetHandler(cache).handle(cmd);
                case GetCommand cmd -> new GetHandler(cache).handle(cmd);
            };
            if (response == null) {
                response = "_\r\n";
            }
            session.write(response);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
    }
}