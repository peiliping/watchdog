package com.github.hubble;


import com.github.hubble.ele.Element;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

import java.util.List;


public class Series<E extends Element> {


    protected String name;

    protected long size;

    protected long mask;

    protected E[] elements;

    @Getter
    protected long maxId;

    @Getter
    protected long interval;

    protected List<SeriesListener<E>> listeners;


    /**
     * @param name     名字
     * @param size     最多保留的element个数，必须是2的N次方
     * @param interval 单位为秒
     */
    public Series(String name, int size, long interval) {

        this.name = name;
        this.size = size;
        Validate.isTrue(Integer.bitCount((int) this.size) == 1);
        this.mask = this.size - 1;
        this.elements = (E[]) new Element[size];
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
            boolean replace = lastElement != null && lastElement.getId() == element.getId();
            if (replace && lastElement.same(element)) {
                return;
            }
            for (SeriesListener<E> listener : this.listeners) {
                listener.onChange(element, replace, this);
            }
        }
    }


    public E getLast() {

        return get(this.maxId);
    }


    public E get(long id) {

        Validate.isTrue(id % this.interval == 0L);
        int position = mod(id / this.interval);
        E e = this.elements[position];
        return e != null && e.getId() == id ? e : null;
    }


    public Series<E> regist(SeriesListener<E> listener) {

        this.listeners.add(listener);
        return this;
    }


    protected int mod(long i) {

        return (int) (i & this.mask);
    }
}
