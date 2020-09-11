package com.github.hubble;


import java.util.concurrent.atomic.AtomicInteger;


public class Bucket<E> {


    private AtomicInteger point = new AtomicInteger(0);

    private int maxSize;

    private FixedList<E> list;

    private long createTime;


    public Bucket(int size) {

        this.maxSize = size;
        this.list = new FixedList<>(size);
        this.createTime = System.currentTimeMillis();
    }


    public boolean add(E e) {

        int index = this.point.getAndIncrement();
        if (index >= this.maxSize) {
            return false;
        }
        this.list.set(index, e);
        return true;
    }


    public boolean isFull() {

        return this.point.get() >= this.maxSize;
    }


    public boolean isEmpty() {

        return this.point.get() == 0;
    }


    public boolean isExpired() {

        return System.currentTimeMillis() - this.createTime >= 1000L;
    }
}
