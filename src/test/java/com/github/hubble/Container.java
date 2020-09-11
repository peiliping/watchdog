package com.github.hubble;


import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;


public class Container<E> {


    private static final int BUCKET_SIZE = 80;

    private static final int MAX_HISTORY_SIZE = 4;

    private volatile Bucket<E> current = new Bucket<>(BUCKET_SIZE);

    private final LinkedList<Bucket<E>> history = Lists.newLinkedList();


    public void add(E e) {

        if (this.current.add(e)) {
            return;
        }
        do {
            updateBucket(false);
        } while (!this.current.add(e));
    }


    private synchronized void updateBucket(boolean force) {

        if (force || this.current.isFull()) {
            if (this.history.size() >= MAX_HISTORY_SIZE) {
                this.history.pollFirst();
            }
            this.history.add(this.current);
            this.current = new Bucket<>(BUCKET_SIZE);
        }
    }


    public synchronized List<Bucket<E>> archiveHistory() {

        if (this.current.isExpired()) {
            updateBucket(true);
        }

        List<Bucket<E>> result = Lists.newArrayList();
        while (this.history.peekFirst() != null) {
            result.add(this.history.pollFirst());
        }
        return result;
    }
}
