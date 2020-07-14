package com.github.hubble.rule.series;


import com.github.hubble.RuleResult;
import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;


public class CandleShockRule extends SeriesRule<CandleET> {


    private double ratio;

    private int count;


    public CandleShockRule(String name, Series<CandleET> series, double ratio, int count) {

        this(name, series, ratio, count, RuleResult.class);
    }


    public CandleShockRule(String name, Series<CandleET> series, double ratio, int count, Class<? extends RuleResult> clazz) {

        super(name, series);
        this.ratio = ratio;
        this.count = count;
        this.clazz = clazz;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        if (super.series.getMaxId() == 0) {
            return false;
        }
        List<CandleET> lastN = Lists.newArrayList();
        long start = super.series.getMaxId() - (this.count - 1) * super.series.getInterval();
        for (long i = start; i <= super.series.getMaxId(); i += super.series.getInterval()) {
            CandleET candleET = super.series.get(i);
            if (candleET != null) {
                lastN.add(candleET);
            }
        }
        CandleET lowCandle = lastN.stream().min(Comparator.comparingDouble(CandleET::getLow)).get();
        CandleET highCandle = lastN.stream().max(Comparator.comparingDouble(CandleET::getHigh)).get();
        CandleET currentCandle = lastN.get(lastN.size() - 1);

        if (currentCandle.getId() != lowCandle.getId() && currentCandle.getId() != highCandle.getId()) {
            return false;
        }

        Double delta = highCandle.getHigh() - lowCandle.getLow();
        Double tRatio = (double) Math.round(delta / lowCandle.getLow() * 10000) / 100;
        if (tRatio < this.ratio) {
            return false;
        }

        String direction = "~";
        if (lowCandle.getId() < highCandle.getId()) {
            direction = "+";
        } else if (lowCandle.getId() > highCandle.getId()) {
            direction = "-";
        } else {
            if (currentCandle.getOpen() < currentCandle.getClose()) {
                direction = "+";
            } else if (currentCandle.getOpen() > currentCandle.getClose()) {
                direction = "-";
            }
        }
        String msg = String.format("%s %s %s %s%s%%", super.name, currentCandle.getId(), currentCandle.getClose(), direction, tRatio);
        results.add(createResult(msg));
        return true;
    }
}
