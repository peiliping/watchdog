package com.github.hubble.rule.series;


import com.github.hubble.ele.Element;
import com.github.hubble.rule.IRule;
import com.github.hubble.series.Series;


public abstract class SeriesRule<E extends Element> extends IRule {


    protected Series<E> series;


    public SeriesRule(String name, Series<E> series) {

        super(name);
        this.series = series;
    }


    @Override protected boolean prepare(long id) {

        return this.series.get(id) != null;
    }
}
