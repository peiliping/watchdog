package com.github.watchdog.stream;


import com.github.watchdog.common.Util;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BarkConsumer extends AbstractConsumer {


    private String[] uids;


    public BarkConsumer(String[] uids) {

        this.uids = uids;
    }


    @Override protected String getFromMsgChannel() {

        return super.msgChannel.getResult();
    }


    @Override protected void handle(String msg) {

        log.info("notify : " + msg);
        for (String uid : this.uids) {
            Util.sendMsg(uid, msg);
        }
    }
}
