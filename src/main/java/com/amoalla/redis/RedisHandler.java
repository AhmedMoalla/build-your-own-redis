package com.amoalla.redis;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class RedisHandler extends IoHandlerAdapter {

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof Object[] arr) {
            RedisCommand command = RedisCommand.parseCommand((String) arr[0]);
            Object[] args = new Object[arr.length - 1];
            System.arraycopy(arr, 1, args, 0, args.length);
            session.write(command.execute(args));
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("IDLE " + session.getIdleCount(status));
    }
}