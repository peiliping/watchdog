package com.github.watchdog.stream;


import com.github.watchdog.common.Util;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BarkConsumer extends AbstractConsumer {


    private String[] uids;

    private boolean inUse;


    public BarkConsumer(String[] uids, boolean inUse) {

        this.uids = uids;
        this.inUse = inUse;
    }


    @Override protected String getFromMsgChannel() {

        return super.msgChannel.getResult();
    }


    @Override protected void handle(String msg) {

        log.info("notify : " + msg);
        if (!this.inUse) {
            return;
        }
        for (String uid : this.uids) {
            Util.sendMsg(uid, msg);
        }
    }
}
