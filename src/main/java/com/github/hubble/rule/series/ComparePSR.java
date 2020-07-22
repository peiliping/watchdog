package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.rule.series.direction.CustomCompare;
import com.github.hubble.ele.Element;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public class ComparePSR<E extends Element> extends PairSeriesRule<E> {


    private CustomCompare<E> customCompare;


    public ComparePSR(String name, Series<E> first, Series<E> second, CustomCompare<E> customCompare) {

        super(name, first, second);
        this.customCompare = customCompare;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        E e1 = super.first.get(id);
        E e2 = super.second.get(id);
        return this.customCompare.exec(e1, e2);
    }
}
