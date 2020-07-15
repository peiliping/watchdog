package com.github.hubble.indicator;


import com.github.hubble.ele.Element;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.LinkedList;


@Getter
public class LastNQueue<E extends Element> {


    private LinkedList<E> list = Lists.newLinkedList();

    private int capacity;


    public LastNQueue(int capacity) {

        this.capacity = capacity;
    }


    public int size() {

        return this.list.size();
    }


    public void add(E e) {

        E last = this.list.peekLast();

        if (last == null) {
            this.list.add(e);
            return;
        }

        int t = Long.compare(last.getId(), e.getId());

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


    public E getFirst() {

        return this.list.peekFirst();
    }


    public E getLast() {

        return this.list.peekLast();
    }
}
