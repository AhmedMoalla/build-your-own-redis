package com.amoalla.redis;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {

    private static final int PORT = 6379;

    public static void main(String[] args) throws IOException {
        SocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        var codecFactory = new TextLineCodecFactory(StandardCharsets.UTF_8, LineDelimiter.CRLF, LineDelimiter.AUTO);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
        acceptor.setHandler(new RedisHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.setReuseAddress(true);
        acceptor.bind(new InetSocketAddress(PORT));
    }
}

class RedisHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof String str && "ping".equals(str)) {
            session.write("+PONG");
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }
}
