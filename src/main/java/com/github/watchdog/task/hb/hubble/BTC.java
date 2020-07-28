package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.indicator.general.CalculatePIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.SeriesParams;
import com.github.watchdog.common.BarkRuleResult;


public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 1d;
    }


    @Override public AbstractHubble init() {

        initShockRL();
        {
            CandleType candleType = CandleType.MIN_60;
            initMARL(candleType, 1800);
            initWRRL(candleType, 1800, 5);

            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
            ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);
            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(closeSeries);
            SeriesParams base = SeriesParams.builder().candleType(candleType).size(bollingPIS.getSize()).build();
            ToNumIS<TernaryNumberET> bollingUpper = new ToNumIS<>(base.createNew("BollingUpper"), value -> value.getFirst());
            bollingUpper.after(bollingPIS);
            CalculatePIS overFlow = new CalculatePIS(base.createNew("OverFlow"), closeSeries, bollingUpper,
                                                     (numberET, numberET2) -> (numberET.getData() - numberET2.getData()) / numberET2.getData() * 100);
            IRule upperPressure = new ThresholdSRL(buildName(candleType, "Bolling_Upper_Pressure"), overFlow, 1d, NumCompareFunction.GTE).overTurn(true).period(3600);
            BarkRuleResult result = new BarkRuleResult("%s.%s的%s线受Bolling指标上轨压迫,后续可能进行调整", super.market, super.name, candleType.name());
            super.rulesManager.addRule(candleType, new Affinity(upperPressure, result));
        }
        initMARL(CandleType.DAY, 3600);
        return this;
    }
}
