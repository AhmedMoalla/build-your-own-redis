package com.amoalla.redis;

import com.amoalla.redis.codec.RedisProtocolCodecFactory;
import com.amoalla.redis.core.EhCacheRedisCache;
import com.amoalla.redis.core.RedisHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.time.Duration;

public class Main {
    private static final int PORT = 6379;

    public static void main(String[] args) throws Exception {

        SocketAcceptor acceptor = new NioSocketAcceptor();
        var filterChain = acceptor.getFilterChain();
//        filterChain.addLast("logger", new LoggingFilter());
        filterChain.addLast("codec", new ProtocolCodecFilter(new RedisProtocolCodecFactory()));
        var cache = new EhCacheRedisCache();
        acceptor.setHandler(new RedisHandler(cache));
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.setReuseAddress(true);
        acceptor.bind(new InetSocketAddress(PORT));
        Runtime.getRuntime().addShutdownHook(new Thread(cache::close));
    }
}
