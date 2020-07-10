package com.github.hubble.rule.series;


import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;


@Slf4j
public class ShockSeriesRule extends SeriesRule<CandleET> {


    private double ratio;

    private int count;


    public ShockSeriesRule(String name, Series<CandleET> series, double ratio, int count) {

        super(name, series);
        this.ratio = ratio;
        this.count = count;
    }


    @Override public boolean isMatched() {

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
        log.warn(String.format("%s %s %s%s%%", super.name, currentCandle.getClose(), direction, tRatio));
        return true;
    }
}
