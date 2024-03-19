package com.amoalla.redis.core;

import com.amoalla.redis.command.EchoCommand;
import com.amoalla.redis.command.PingCommand;
import com.amoalla.redis.command.RedisCommand;
import com.amoalla.redis.command.SetCommand;
import com.amoalla.redis.handler.EchoHandler;
import com.amoalla.redis.handler.PingHandler;
import com.amoalla.redis.handler.SetHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class RedisHandler extends IoHandlerAdapter {
    @Override
    public void messageReceived(IoSession session, Object message) {
        if (message instanceof RedisCommand command) {
            Object response = switch (command) {
                case EchoCommand cmd -> new EchoHandler().handle(cmd);
                case PingCommand cmd -> new PingHandler().handle(cmd);
                case SetCommand cmd -> new SetHandler().handle(cmd);
            };
            session.write(response);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
    }
}