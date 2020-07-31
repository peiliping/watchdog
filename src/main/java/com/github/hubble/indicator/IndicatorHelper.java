package com.github.hubble.indicator;


import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.NumberET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.function.PISFuncs;
import com.github.hubble.indicator.general.*;
import com.github.hubble.indicator.specific.*;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.SeriesParams;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.ToDoubleFunction;


public class IndicatorHelper {


    private static Map<String, ToNumIS<CandleET>> CANDLE_TONUM_CACHE = Maps.newHashMap();

    private static Map<String, PolarIS> POLAR_IS_CACHE = Maps.newHashMap();


    private static ToNumIS<CandleET> create_TONUM_IS(CandleSeries candleSeries, String name, ToDoubleFunction<CandleET> function) {

        String key = StringUtils.joinWith(".", candleSeries.getFullName(), name);
        ToNumIS<CandleET> is = CANDLE_TONUM_CACHE.get(key);
        if (is == null) {
            SeriesParams params = SeriesParams.builder().name(name).candleType(candleSeries.getCandleType()).size(candleSeries.getSize()).build();
            is = new ToNumIS<>(params, function);
            is.after(candleSeries);
            CANDLE_TONUM_CACHE.put(key, is);
        }
        return is;
    }


    public static ToNumIS<CandleET> create_CLOSE_IS(CandleSeries candleSeries) {

        return create_TONUM_IS(candleSeries, "Close", candleET -> candleET.getClose());
    }


    public static ToNumIS<CandleET> create_HIGH_IS(CandleSeries candleSeries) {

        return create_TONUM_IS(candleSeries, "High", candleET -> candleET.getHigh());
    }


    public static ToNumIS<CandleET> create_LOW_IS(CandleSeries candleSeries) {

        return create_TONUM_IS(candleSeries, "Low", candleET -> candleET.getLow());
    }


    public static PolarIS create_POLAR_IS(CandleSeries candleSeries, int step) {

        String name = String.format("Polar(%s)", step);
        String key = StringUtils.joinWith(".", candleSeries.getFullName(), name);
        PolarIS polarIS = POLAR_IS_CACHE.get(key);
        if (polarIS == null) {
            SeriesParams params = SeriesParams.builder().name(name).candleType(candleSeries.getCandleType()).size(candleSeries.getSize()).build();
            polarIS = new PolarIS(params, step);
            polarIS.after(candleSeries);
        }
        return polarIS;
    }


    public static MAIS create_MA_IS(IndicatorSeries<?, NumberET> indicatorSeries, int step) {

        String name = String.format("MA(%s)", step);
        SeriesParams params = SeriesParams.builder().name(name).candleType(indicatorSeries.getCandleType()).size(indicatorSeries.getSize()).build();
        MAIS ma = new MAIS(params, step);
        ma.after(indicatorSeries);
        return ma;
    }


    public static EMAIS create_EMA_IS(IndicatorSeries<?, NumberET> indicatorSeries, int step, double multiplier) {

        String name = String.format("EMA(%s,%s)", step, multiplier);
        SeriesParams params = SeriesParams.builder().name(name).candleType(indicatorSeries.getCandleType()).size(indicatorSeries.getSize()).build();
        EMAIS ema = new EMAIS(params, step, multiplier);
        ema.after(indicatorSeries);
        return ema;
    }


    public static EMAIS create_EMA_IS(IndicatorSeries<?, NumberET> indicatorSeries, int step) {

        return create_EMA_IS(indicatorSeries, step, 2d);
    }


    public static DeltaIS create_Delta_IS(IndicatorSeries<?, NumberET> indicatorSeries) {

        SeriesParams params = SeriesParams.builder().name("Delta").candleType(indicatorSeries.getCandleType()).size(indicatorSeries.getSize()).build();
        DeltaIS delta = new DeltaIS(params);
        delta.after(indicatorSeries);
        return delta;
    }


    public static STDDIS create_STDD_IS(IndicatorSeries<?, NumberET> indicatorSeries, int step) {

        String name = String.format("STDD(%s)", step);
        SeriesParams params = SeriesParams.builder().name(name).candleType(indicatorSeries.getCandleType()).size(indicatorSeries.getSize()).build();
        STDDIS stdd = new STDDIS(params, step);
        stdd.after(indicatorSeries);
        return stdd;
    }


    public static BollingPIS create_Bolling_PIS(ToNumIS<CandleET> closeIS, int step) {

        double multiplier = 2d;
        STDDIS stdd = create_STDD_IS(closeIS, step);
        MAIS ma = create_MA_IS(closeIS, step);
        String name = String.format("Bolling(%s)", multiplier);
        SeriesParams params = SeriesParams.builder().name(name).candleType(closeIS.getCandleType()).size(closeIS.getSize()).build();
        return new BollingPIS(params, multiplier, stdd, ma);
    }


    public static ToNumIS<TernaryNumberET> create_Bolling_Up_IS(BollingPIS bollingPIS) {

        SeriesParams params = SeriesParams.builder().name("BollingUp").candleType(bollingPIS.getCandleType()).size(bollingPIS.getSize()).build();
        ToNumIS<TernaryNumberET> bollingUp = new ToNumIS<>(params, value -> value.getFirst());
        bollingUp.after(bollingPIS);
        return bollingUp;
    }


    public static ToNumIS<TernaryNumberET> create_Bolling_Down_IS(BollingPIS bollingPIS) {

        SeriesParams params = SeriesParams.builder().name("BollingDown").candleType(bollingPIS.getCandleType()).size(bollingPIS.getSize()).build();
        ToNumIS<TernaryNumberET> bollingDown = new ToNumIS<>(params, value -> value.getThird());
        bollingDown.after(bollingPIS);
        return bollingDown;
    }


    public static MACDPIS create_MACD_PIS(ToNumIS<CandleET> closeIS) {

        EMAIS shortEma = create_EMA_IS(closeIS, 12);
        EMAIS longEma = create_EMA_IS(closeIS, 26);

        SeriesParams difParams = SeriesParams.builder().name("DIF").candleType(closeIS.getCandleType()).size(closeIS.getSize()).build();
        CalculatePIS dif = new CalculatePIS(difParams, shortEma, longEma, PISFuncs.MINUS);
        EMAIS dea = create_EMA_IS(dif, 9);

        SeriesParams params = SeriesParams.builder().name("MACD").candleType(closeIS.getCandleType()).size(closeIS.getSize()).build();
        return new MACDPIS(params, dif, dea);
    }


    public static RSIPIS create_RSI_PIS(ToNumIS<CandleET> closeIS) {

        SeriesParams base = SeriesParams.builder().candleType(closeIS.getCandleType()).size(closeIS.getSize()).build();
        DeltaIS deltaIS = create_Delta_IS(closeIS);

        ToNumIS<NumberET> up = new ToNumIS<>(base.createNew("Positive"), numberET -> Math.max(numberET.getData(), 0));
        up.after(deltaIS);
        EMAIS emaUP = create_EMA_IS(up, 14, 1d);

        ToNumIS<NumberET> total = new ToNumIS<>(base.createNew("Total"), numberET -> Math.abs(numberET.getData()));
        total.after(deltaIS);
        EMAIS emaTotal = create_EMA_IS(total, 14, 1d);

        return new RSIPIS(base.createNew("RSI"), emaUP, emaTotal);
    }


    public static KDJPIS create_KDJ_PIS(PolarIS polarIS) {

        SeriesParams base = SeriesParams.builder().candleType(polarIS.getCandleType()).size(polarIS.getSize()).build();
        ToNumIS<TernaryNumberET> rsvIS = new ToNumIS<>(base.createNew("RSV"), ele -> (ele.getSecond() - ele.getThird()) / (ele.getFirst() - ele.getThird()) * 100);
        rsvIS.after(polarIS);

        MAIS k = create_MA_IS(rsvIS, 1);
        MAIS d = create_MA_IS(k, 3);
        return new KDJPIS(base.createNew("KDJ"), k, d);
    }


    public static WRIS create_WR_IS(CandleSeries candleSeries, int step) {

        PolarIS polarIS = create_POLAR_IS(candleSeries, step);
        SeriesParams params = SeriesParams.builder().name("WR").candleType(polarIS.getCandleType()).size(polarIS.getSize()).build();
        WRIS wr = new WRIS(params);
        wr.after(polarIS);
        return wr;
    }
}
