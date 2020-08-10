package com.github.hubble.common;


import java.util.concurrent.atomic.AtomicLong;


public class Clock {


    private final AtomicLong timeSeq = new AtomicLong(0);


    public void update(long t) {

        this.timeSeq.set(t);
    }


    public long get() {

        return this.timeSeq.get();
    }
}
