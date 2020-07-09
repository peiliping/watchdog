package com.github.hubble.sr;


import com.github.hubble.ele.Element;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;

import java.util.List;


public class Series<E extends Element> {


    protected String name;

    protected long size;

    protected long mask;

    protected E[] elements;

    protected long maxId;

    protected long interval;

    protected List<SeriesListener<E>> listeners;


    public Series(String name, int size, long interval) {

        this.name = name;
        this.size = size;
        Validate.isTrue(Integer.bitCount((int) this.size) == 1);
        this.mask = this.size - 1;
        this.elements = (E[]) new Object[size];
        this.maxId = 0L;
        this.interval = interval;
        this.listeners = Lists.newArrayList();
    }


    public void add(E element) {

        Validate.isTrue(element.getId() % this.interval == 0L);
        Validate.isTrue(element.getId() >= this.maxId);
        this.maxId = element.getId();
        int position = mod(element.getId() / this.interval);
        Element lastElement = this.elements[position];
        if (lastElement == null || lastElement.compareTo(element) <= 0) {
            this.elements[position] = element;
            for (SeriesListener<E> listener : this.listeners) {
                boolean replace = lastElement != null && lastElement.getId() == element.getId();
                listener.onChange(element, replace);
            }
        }
    }


    private int mod(long i) {

        return (int) (i & this.mask);
    }
}
