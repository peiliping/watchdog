package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.signal.Signal;
import com.github.hubble.signal.SignalRuleResult;
import com.github.hubble.trend.TrendEntity;
import com.github.hubble.trend.constants.Period;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 1d;
    }


    @Override public AbstractHubble init() {

        initShockRL();
        initShortTermRules(CandleType.MIN_5);
        initMediumTermRules(CandleType.MIN_60);
        initLongTermRules(CandleType.HOUR_4);

        {
            CandleType candleType = CandleType.MIN_15;
            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 1);
            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);

            IRule bollingSupport = new BollingSupportPSR(buildName(candleType, "Bolling_Down_Support"), polarIS, bollingPIS).overTurn(true);
            super.rulesManager.addRule(candleType, new Affinity(bollingSupport, new SignalRuleResult("Bolling下轨支撑", Signal.INPUT, this)));
        }
        return this;
    }


    @Override public void spark(CandleType candleType, Signal signal) {

        double currentPrice = super.candleSeriesManager.getOrCreateCandleSeries(candleType).getLast().getClose();
        TrendEntity st = super.trendManager.get(Period.SHORT);
        TrendEntity mt = super.trendManager.get(Period.MEDIUM);
        TrendEntity lt = super.trendManager.get(Period.LONG);
        log.warn("{}.{} Spark : {} {} , {} {} {}", super.market, super.name, currentPrice, signal, st.toString(), mt.toString(), lt.toString());
    }
}
