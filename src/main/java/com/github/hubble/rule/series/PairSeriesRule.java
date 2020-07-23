package com.github.hubble.rule.series;


import com.github.hubble.ele.Element;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.series.Series;

import java.util.List;


public abstract class PairSeriesRule<E extends Element> extends IRule {


    protected Series<E> first;

    protected Series<E> second;


    public PairSeriesRule(String name, Series<E> first, Series<E> second) {

        super(name);
        this.first = first;
        this.second = second;
    }


    @Override public boolean prepare(long id, List<RuleResult> results) {

        E e1 = this.first.get(id);
        E e2 = this.second.get(id);
        return e1 != null && e2 != null;
    }
}
