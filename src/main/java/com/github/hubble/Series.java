package com.github.hubble;


import com.github.hubble.ele.Element;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Slf4j
public class Series<E extends Element> {


    @Getter
    protected String parentName;

    @Getter
    protected final String name;

    protected final long size;

    protected final long mask;

    protected final E[] elements;

    protected final List<SeriesUpsertListener<E>> upsertListeners;

    protected final List<SeriesTimeListener> timeListeners;

    @Getter
    protected final long interval;

    @Getter
    protected long maxId = 0L;

    protected long sequence = 0;


    public Series(SeriesParams params) {

        this.name = params.getName();
        this.size = params.getSize();
        Validate.isTrue(Integer.bitCount((int) this.size) == 1);
        this.mask = this.size - 1;
        this.elements = (E[]) new Element[(int) this.size];
        this.upsertListeners = Lists.newArrayList();
        this.timeListeners = Lists.newArrayList();
        this.interval = params.getInterval();
    }


    public String getFullName() {

        if (this.parentName == null) {
            return this.name;
        }
        return this.parentName + "." + this.name;
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
            log.debug("{} add element {} .", getFullName(), element.toString());
        }
        for (SeriesUpsertListener<E> listener : this.upsertListeners) {
            listener.onChange(this.sequence, element, replace, this);
        }
        for (SeriesTimeListener listener : this.timeListeners) {
            listener.onTime(this.sequence, element.getId());
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


    public void bindUpsertListener(SeriesUpsertListener<E>... listeners) {

        for (SeriesUpsertListener<E> listener : listeners) {
            this.upsertListeners.add(listener);
        }
    }


    public void bindTimeListener(SeriesTimeListener... listeners) {

        for (SeriesTimeListener listener : listeners) {
            this.timeListeners.add(listener);
        }
    }


    protected int getPosition(long id) {

        Validate.isTrue(id % this.interval == 0L);
        long i = id / this.interval;
        return (int) (i & this.mask);
    }
}
