package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.Element;
import com.github.hubble.rule.IRule;


public abstract class PairSeriesRule<E extends Element> extends IRule {


    protected Series<E> first;

    protected Series<E> second;


    public PairSeriesRule(String name, Series<E> first, Series<E> second) {

        super(name);
        this.first = first;
        this.second = second;
    }
}