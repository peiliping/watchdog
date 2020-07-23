package com.github.hubble.indicator;


import com.github.hubble.ele.Element;
import com.github.hubble.ele.NumberET;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;
import com.github.hubble.series.SeriesTimeListener;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Slf4j
public abstract class PairIndicatorSeries<F extends IndicatorSeries, S extends IndicatorSeries, R extends Element>
        extends IndicatorSeries<NumberET, R> implements SeriesTimeListener {


    protected F first;

    protected S second;


    public PairIndicatorSeries(SeriesParams params, F first, S second) {

        super(params);
        this.first = first;
        this.second = second;
        super.parentName = "[" + this.first.getFullName() + "," + this.second.getFullName() + "]";
        Series st = analyze(this.first, this.second);
        st.bindTimeListener(this);
        log.info("{} [{} , {}] bind on {} .", getName(), this.first.getName(), this.second.getName(), st.getName());
    }


    private Series analyze(IndicatorSeries f, IndicatorSeries s) {

        List<Series> left = analyze(f);
        List<Series> right = analyze(s);
        Validate.isTrue(left.size() > 0 && right.size() > 0);
        for (Series ss : left) {
            if (right.indexOf(ss) >= 0) {
                return ss;
            }
        }
        Validate.isTrue(false);
        return null;
    }


    private List<Series> analyze(IndicatorSeries is) {

        List<Series> isr = Lists.newArrayList();
        isr.add(is);
        Series k = is.parentSeries;
        while (k != null) {
            isr.add(k);
            if (k instanceof IndicatorSeries) {
                k = ((IndicatorSeries) k).parentSeries;
            } else {
                break;
            }
        }
        return isr;
    }


    @Override public void after(Series series) {

        Validate.isTrue(false, "forbid");
    }


    @Override public final void onChange(long seq, NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

        Validate.isTrue(false, "forbid");
    }


    @Override protected final void onChange(NumberET ele, boolean updateOrInsert, Series<NumberET> series) {

    }


    @Override public void onTime(long seq, long timeId) {

        if (seq > super.lastSequence) {
            super.lastSequence = seq;
            onTime(timeId);
        }
    }


    protected abstract void onTime(long timeId);
}
