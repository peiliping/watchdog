package com.github.hubble.strategy;


import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesParams;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;


public abstract class BaseStrategy {


    protected String market;

    protected String symbol;

    protected Map<CandleType, CandleSeries> candles = Maps.newHashMap();


    public void initCandles(CandleType candleType, List<CandleET> candleETList) {

        Validate.isTrue(!this.candles.containsKey(candleType));
        SeriesParams params = SeriesParams.builder().name(candleType.name()).size(128).interval(candleType.interval).build();
        CandleSeries series = new CandleSeries(params, StringUtils.joinWith(".", this.market, this.symbol));
        this.candles.put(candleType, series);
        for (CandleET candleET : candleETList) {
            series.add(candleET);
        }
    }


    public void addCandleET(CandleType candleType, CandleET candleET) {

        Series<CandleET> series = this.candles.get(candleType);
        if (candleET.getId() >= series.getMaxId()) {
            series.add(candleET);
        }
    }

}
