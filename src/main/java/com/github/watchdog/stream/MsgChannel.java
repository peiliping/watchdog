package com.github.watchdog.stream;


import com.github.watchdog.common.Util;
import lombok.Getter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class MsgChannel {


    public static final String CMD_RESTART = "$RESTART$";

    private LinkedBlockingQueue<String> input = new LinkedBlockingQueue<>();

    private LinkedBlockingQueue<String> result = new LinkedBlockingQueue<>();

    private static MsgChannel instance;

    @Getter
    private long lastMsgTime = Util.nowMS();


    private MsgChannel() {

    }


    public static MsgChannel getInstance() {

        if (instance != null) {
            return instance;
        }

        synchronized (MsgChannel.class) {
            if (instance == null) {
                instance = new MsgChannel();
            }
        }

        return instance;
    }


    public void addInput(String s) {

        this.lastMsgTime = Util.nowMS();
        this.input.add(s);
    }


    public String getInput() {

        try {
            return this.input.poll(100L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }


    public void addResult(String s) {

        this.result.add(s);
    }


    public String getResult() {

        try {
            return this.result.poll(100L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }
}
