package com.github.hubble.series;


import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Getter
public class CandleSeriesManager {


    protected final Map<CandleType, CandleSeries> candles = Maps.newHashMap();

    protected final int elementSize;


    public CandleSeriesManager(int elementSize, Set<CandleType> candleTypeSet) {

        this.elementSize = elementSize;
        for (CandleType ct : candleTypeSet) {
            getOrCreateCandleSeries(ct);
        }
    }


    private CandleSeries getOrCreateCandleSeries(CandleType candleType) {

        CandleSeries series = this.candles.get(candleType);
        if (series != null) {
            return series;
        }

        SeriesParams params = SeriesParams.builder().name(candleType.name()).size(this.elementSize).candleType(candleType).build();
        series = new CandleSeries(params);
        this.candles.put(candleType, series);
        return series;
    }


    public CandleSeries getCandleSeries(CandleType candleType) {

        return this.candles.get(candleType);
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


    public CandleET getLastCandleET(CandleType candleType) {

        CandleSeries series = this.candles.get(candleType);
        return series.getLast();
    }


    public SeriesAggListener candleSeriesBridge(CandleType sourceType, CandleType targetType) {

        SeriesAggListener aggListener = new SeriesAggListener(this.candles.get(targetType));
        this.candles.get(sourceType).bindUpsertListener(aggListener);
        return aggListener;
    }
}
