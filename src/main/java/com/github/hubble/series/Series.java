package com.github.hubble.series;


import com.github.hubble.common.CandleType;
import com.github.hubble.ele.Element;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Slf4j
public class Series<E extends Element> {


    @Getter
    protected final String name;

    @Getter
    protected final CandleType candleType;

    protected final E[] elements;

    protected final long size;

    protected final long mask;

    protected final List<SeriesUpsertListener<E>> upsertListeners;

    protected final List<SeriesTimeListener> timeListeners;

    protected String parentName;

    private long maxId = 0L;

    private long sequence = 0;


    public Series(SeriesParams params) {

        this.name = params.getName();
        this.size = params.getSize();
        Validate.isTrue(Integer.bitCount((int) this.size) == 1);
        this.mask = this.size - 1;
        this.elements = (E[]) new Element[(int) this.size];
        this.upsertListeners = Lists.newArrayList();
        this.timeListeners = Lists.newArrayList();
        this.candleType = params.getCandleType();
    }


    public int getSize() {

        return (int) size;
    }


    public String getFullName() {

        if (this.parentName == null) {
            return this.name;
        }
        return StringUtils.joinWith(".", this.parentName, this.name);
    }


    protected int getPosition(long id) {

        this.candleType.validate(id);
        long i = (id + this.candleType.offset) / this.candleType.interval;
        return (int) (i & this.mask);
    }


    public void add(E element) {

        if (element.getId() < this.maxId) {
            return;
        }

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


    public E getLast() {

        return get(this.maxId);
    }


    public E getBefore(long id) {

        return get(id - this.candleType.interval);
    }


    public E getBefore(long id, int n) {

        return get(id - this.candleType.interval * n);
    }


    public E getAfter(long id) {

        return get(id + this.candleType.interval);
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
}
