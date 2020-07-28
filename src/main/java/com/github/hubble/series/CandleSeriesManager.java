package com.github.hubble.series;


import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;


@Getter
public class CandleSeriesManager {


    protected String market;

    protected String symbol;

    protected int elementSize;

    protected Map<CandleType, CandleSeries> candles = Maps.newHashMap();


    public CandleSeriesManager(String market, String symbol, int elementSize) {

        this.market = market;
        this.symbol = symbol;
        this.elementSize = elementSize;
    }


    public CandleSeries getOrCreateCandleSeries(CandleType candleType) {

        CandleSeries series = this.candles.get(candleType);
        if (series != null) {
            return series;
        }

        SeriesParams params = SeriesParams.builder().name(candleType.name()).size(this.elementSize).candleType(candleType).build();
        series = new CandleSeries(params, StringUtils.joinWith(".", this.market, this.symbol));
        this.candles.put(candleType, series);
        return series;
    }


    public void addCandleETList(CandleType candleType, List<CandleET> candleETList) {

        CandleSeries series = this.candles.get(candleType);
        Validate.notNull(series);
        for (CandleET candleET : candleETList) {
            series.add(candleET);
        }
    }


    public void addCandleET(CandleType candleType, CandleET candleET) {

        Series<CandleET> series = this.candles.get(candleType);
        Validate.notNull(series);
        series.add(candleET);
    }
}
