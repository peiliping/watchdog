package com.github.hubble.rule.series.pair;


import com.github.hubble.ele.Element;
import com.github.hubble.rule.IRule;
import com.github.hubble.series.Series;


public abstract class PairSeriesRule<E extends Element> extends IRule {


    protected final Series<E> first;

    protected final Series<E> second;

    protected int continuousStep = 1;


    public PairSeriesRule(String name, Series<E> first, Series<E> second) {

        super(name);
        this.first = first;
        this.second = second;
    }


    @Override public boolean prepare(long id) {

        if (this.continuousStep == 1) {
            E e1 = this.first.get(id);
            E e2 = this.second.get(id);
            return e1 != null && e2 != null;
        }

        int x = this.continuousStep;
        while (x-- > 0) {
            E e1 = this.first.getBefore(id, x);
            E e2 = this.second.getBefore(id, x);
            if (e1 == null || e2 == null) {
                return false;
            }
        }
        return true;
    }
}
