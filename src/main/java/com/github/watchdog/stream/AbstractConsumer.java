package com.github.watchdog.stream;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractConsumer implements Runnable {


    protected final MsgChannel msgChannel = MsgChannel.getInstance();


    @Override public void run() {

        while (true) {
            String msg = getFromMsgChannel();
            if (msg == null) {
                continue;
            }
            try {
                handle(msg);
            } catch (Throwable e) {
                log.error("handle msg error : ", e);
                log.error("msg : " + msg);
            }
        }
    }


    protected abstract void handle(String msg);


    protected String getFromMsgChannel() {

        return this.msgChannel.getInput();
    }
}
