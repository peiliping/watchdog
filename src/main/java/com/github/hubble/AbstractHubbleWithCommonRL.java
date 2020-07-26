package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.SeriesParams;
import com.github.watchdog.common.BarkRuleResult;


public abstract class AbstractHubbleWithCommonRL extends AbstractHubble {


    protected double shockRatio;


    public AbstractHubbleWithCommonRL(String market, String name) {

        super(market, name);
        this.shockRatio = 1d;
    }


    //5分钟震荡达到N%
    protected void initShockSRL() {

        CandleSeries min1_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.MIN_1);
        PolarIS min1_PolarIS = IndicatorHelper.create_POLAR_IS(min1_CS, 5);
        SeriesParams params = SeriesParams.builder().name("ShockRate").interval(min1_CS.getInterval()).size(min1_CS.getSize()).build();
        ToNumIS<TernaryNumberET> shockIS = new ToNumIS<>(params, value -> (double) Math.round((value.getFirst() - value.getThird()) / value.getThird() * 10000) / 100);
        shockIS.after(min1_PolarIS);
        IRule shockSRL = new ThresholdSRL(buildName("ShockSRL"), shockIS, this.shockRatio, NumCompareFunction.GTE).overTurn(true).period(900);
        BarkRuleResult result = new BarkRuleResult("%s is shocking (>= %s%%)", buildName("price"), this.shockRatio);
        super.rulesManager.addRule(CandleType.MIN_1, new Affinity(shockSRL, result));
    }

}
