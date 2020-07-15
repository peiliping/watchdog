package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.Element;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public abstract class SeriesRule<E extends Element> extends IRule {


    protected Series<E> series;


    public SeriesRule(String name, Series<E> series) {

        super(name);
        this.series = series;
    }


    @Override public boolean matchRule(long id, List<RuleResult> results) {

        if (this.series.get(id) == null) {
            return false;
        }
        return super.matchRule(id, results);
    }
}
