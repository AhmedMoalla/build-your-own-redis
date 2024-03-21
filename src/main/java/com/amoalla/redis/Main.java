package com.amoalla.redis;

import com.amoalla.redis.codec.RedisProtocolCodecFactory;
import com.amoalla.redis.core.EhCacheRedisCache;
import com.amoalla.redis.core.RedisHandler;
import com.amoalla.redis.handler.info.InfoProvider;
import com.amoalla.redis.replication.ReplicationManager;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        Args parsedArgs = Args.parse(args);
        SocketAcceptor acceptor = new NioSocketAcceptor();
        var filterChain = acceptor.getFilterChain();
//        filterChain.addLast("logger", new LoggingFilter());
        filterChain.addLast("codec", new ProtocolCodecFilter(new RedisProtocolCodecFactory()));
        var cache = new EhCacheRedisCache();
        var replicationManager = new ReplicationManager();
        List<InfoProvider> infoProviders = List.of(replicationManager);
        acceptor.setHandler(new RedisHandler(cache, infoProviders));
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.setReuseAddress(true);
        acceptor.bind(new InetSocketAddress(parsedArgs.port()));
        Runtime.getRuntime().addShutdownHook(new Thread(cache::close));
    }

    private record Args(int port) {
        static Args parse(String[] args) {
            int port = 6379;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--port") && i + 1 < args.length) {
                    port = Integer.parseInt(args[i + 1]);
                    break;
                }
            }

            return new Args(port);
        }
    }
}
