package com.github.hubble.rule.series;


import com.github.hubble.ele.Element;
import com.github.hubble.rule.IRule;
import com.github.hubble.series.Series;


public abstract class SeriesRule<E extends Element> extends IRule {


    protected final Series<E> series;

    protected int continuousStep = 1;


    public SeriesRule(String name, Series<E> series) {

        super(name);
        this.series = series;
    }


    @Override protected boolean prepare(long id) {

        if (this.continuousStep == 1) {
            return this.series.get(id) != null;
        }

        int x = this.continuousStep;
        while (x-- > 0) {
            if (this.series.getBefore(id, x) == null) {
                return false;
            }
        }
        return true;
    }
}
