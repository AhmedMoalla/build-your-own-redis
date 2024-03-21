package com.amoalla.redis.core;

import com.amoalla.redis.command.*;
import com.amoalla.redis.handler.*;
import com.amoalla.redis.handler.info.InfoHandler;
import com.amoalla.redis.handler.info.InfoProvider;
import com.amoalla.redis.replication.ReplicationManager;
import com.amoalla.redis.types.NullValue;
import lombok.RequiredArgsConstructor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.List;

@RequiredArgsConstructor
public class RedisHandler extends IoHandlerAdapter {

    private final RedisCache cache;
    private final ReplicationManager replicationManager;
    private final List<InfoProvider> infoProviders;

    @Override
    public void messageReceived(IoSession session, Object message) {
        if (message instanceof RedisCommand command) {
            Object response = switch (command) {
                case EchoCommand cmd -> new EchoHandler().handle(cmd);
                case PingCommand cmd -> new PingHandler().handle(cmd);
                case SetCommand cmd -> new SetHandler(cache).handle(cmd);
                case GetCommand cmd -> new GetHandler(cache).handle(cmd);
                case InfoCommand cmd -> new InfoHandler(infoProviders).handle(cmd);
                case ReplConfCommand cmd -> new ReplConfHandler().handle(cmd);
                case PSyncCommand cmd -> new PsyncHandler(replicationManager).handle(cmd);
            };
            if (response == null) {
                response = NullValue.INSTANCE;
            }
            session.write(response);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();
    }
}