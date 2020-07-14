package com.github.hubble.rule.series;


import com.github.hubble.RuleResult;
import com.github.hubble.Series;
import com.github.hubble.ele.BooleanET;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class BooleanSeriesRule extends SeriesRule<BooleanET> {


    public BooleanSeriesRule(String name, Series<BooleanET> series) {

        super(name, series);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        BooleanET singleBooleanET = super.series.get(id);
        if (log.isDebugEnabled()) {
            log.debug("{} series result : {} .", super.name, singleBooleanET);
        }
        return singleBooleanET != null && singleBooleanET.isData();
    }
}
