package com.amoalla.redis.replication;

import com.amoalla.redis.codec.RedisProtocolCodecFactory;
import com.amoalla.redis.command.PingCommand;
import com.amoalla.redis.handler.info.InfoProvider;
import com.amoalla.redis.handler.info.InfoType;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.security.SecureRandom;

public class ReplicationManager extends IoHandlerAdapter implements InfoProvider {

    private final String replicationId = generateReplicationId(40);
    private final ReplicationConfig config;
    private IoSession masterSession;

    public ReplicationManager(ReplicationConfig config) {
        this.config = config;

        if (config.isSlave()) {
            connectToMaster(config.masterHost(), config.masterPort());
        }
    }

    private void connectToMaster(String masterHost, int masterPort) {
        IoConnector connector = new NioSocketConnector();
        connector.setHandler(this);
        var filterChain = connector.getFilterChain();
//        filterChain.addLast("logger", new LoggingFilter());
        filterChain.addLast("codec", new ProtocolCodecFilter(new RedisProtocolCodecFactory()));
        ConnectFuture future = connector.connect(new InetSocketAddress(masterHost, masterPort));
        future.awaitUninterruptibly();
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        if (masterSession != null) {
            throw new IllegalStateException("Already connected to master");
        }
        masterSession = session;

        session.write(new PingCommand());
    }

    private String role() {
        return config.isSlave() ? "slave" : "master";
    }

    @Override
    public String info() {
        return STR."""
        # \{type().displayName()}\r
        role:\{role()}\r
        master_replid:\{replicationId}\r
        master_repl_offset:0""";
    }

    @Override
    public InfoType type() {
        return InfoType.REPLICATION;
    }


    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private static String generateReplicationId(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }

        return builder.toString();
    }

}
