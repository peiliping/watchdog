package com.github.watchdog.dataobject;


import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.LinkedList;


public class LastNQueue<E extends Comparable> {


    @Getter
    private LinkedList<E> list = Lists.newLinkedList();

    private int capacity;


    public LastNQueue(int size) {

        this.capacity = size;
    }


    public void add(E e) {

        E last = this.list.peekLast();

        if (last == null) {
            this.list.add(e);
            return;
        }

        int t = last.compareTo(e);

        if (t > 0) {
            return;
        }

        if (t == 0) {
            this.list.pollLast();
            this.list.add(e);
            return;
        }

        if (t < 0) {
            if (this.list.size() >= this.capacity) {
                this.list.pollFirst();
            }
            this.list.add(e);
        }
    }
}
