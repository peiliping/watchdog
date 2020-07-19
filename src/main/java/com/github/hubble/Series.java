package com.github.hubble;


import com.github.hubble.ele.Element;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Slf4j
public class Series<E extends Element> {


    protected final String name;

    protected final long size;

    protected final long mask;

    protected final E[] elements;

    protected final List<SeriesListener<E>> listeners;

    @Getter
    protected final long interval;

    @Getter
    protected long maxId = 0L;

    protected long sequence = 0;


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
        this.listeners = Lists.newArrayList();
        this.interval = interval;
    }


    public void add(E element) {

        Validate.isTrue(element.getId() >= this.maxId);

        int position = getPosition(element.getId());
        Element lastElement = this.elements[position];
        this.elements[position] = element;
        this.maxId = element.getId();

        boolean replace = lastElement != null && lastElement.getId() == element.getId();
        if (replace && lastElement.same(element)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("{} add element {} .", this.name, element.toString());
        }
        for (SeriesListener<E> listener : this.listeners) {
            listener.onChange(this.sequence, element, replace, this);
        }
        this.sequence++;
    }


    public E get(long id) {

        int position = getPosition(id);
        E e = this.elements[position];
        return e != null && e.getId() == id ? e : null;
    }


    public E getBefore(long id) {

        return get(id - this.interval);
    }


    public void bind(SeriesListener<E>... listeners) {

        for (SeriesListener<E> listener : listeners) {
            this.listeners.add(listener);
        }
    }


    protected int getPosition(long id) {

        Validate.isTrue(id % this.interval == 0L);
        long i = id / this.interval;
        return (int) (i & this.mask);
    }
}
