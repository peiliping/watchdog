package com.github.hubble.rule.series;


import com.github.hubble.rule.RuleResult;
import com.github.hubble.Series;
import com.github.hubble.ele.CustomCompare;
import com.github.hubble.ele.Element;

import java.util.List;


public class CompareSeriesRule<E extends Element> extends PairSeriesRule<E> {


    private CustomCompare<E> customCompare;


    public CompareSeriesRule(String name, Series<E> first, Series<E> second, CustomCompare<E> customCompare) {

        super(name, first, second);
        this.customCompare = customCompare;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        E e1 = super.first.get(id);
        E e2 = super.second.get(id);
        if (e1 != null && e2 != null) {
            return this.customCompare.exec(e1, e2);
        }
        return false;
    }
}
