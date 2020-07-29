package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.MAIS;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.indicator.specific.WRIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.series.pair.NumComparePSR;
import com.github.hubble.rule.series.pair.CrossPSR;
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


    // 最近M分钟震荡达到N%
    protected void initShockRL() {

        CandleSeries min1_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.MIN_1);
        PolarIS min1_PolarIS = IndicatorHelper.create_POLAR_IS(min1_CS, 5);
        SeriesParams params = SeriesParams.builder().name("ShockRate").candleType(min1_CS.getCandleType()).size(min1_CS.getSize()).build();
        ToNumIS<TernaryNumberET> shockIS = new ToNumIS<>(params, value -> (double) Math.round((value.getFirst() - value.getThird()) / value.getThird() * 10000) / 100);
        shockIS.after(min1_PolarIS);
        IRule shockSRL = new ThresholdSRL(buildName(CandleType.MIN_1, "ShockSRL"), shockIS, this.shockRatio, NumCompareFunction.GTE).overTurn(true).period(900);
        BarkRuleResult result = new BarkRuleResult("%s.%s is shocking (>= %s%%)", super.market, super.name, this.shockRatio);
        super.rulesManager.addRule(CandleType.MIN_1, new Affinity(shockSRL, result));
    }


    // k线MA指标趋势
    protected void initMARL(CandleType candleType, int period) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(candleSeries);

        MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
        MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);
        MAIS ma30 = IndicatorHelper.create_MA_IS(closeSeries, 30);

        IRule risingRule = new NumComparePSR(buildName(candleType, "NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT)
                .and(new NumComparePSR(buildName(candleType, "NCPSR_MA10VS30"), ma10, ma30, NumCompareFunction.GT))
                .and(new CrossPSR.RisingCrossPSR(buildName(candleType, "RCPSR_MA05VS10"), ma05, ma10)
                             .or(new CrossPSR.RisingCrossPSR(buildName(candleType, "RCPSR_MA10VS30"), ma10, ma30)))
                .overTurn(true).period(period);
        BarkRuleResult rResult = new BarkRuleResult("%s.%s的%s线MA指标呈现多头排列上升趋势", super.market, super.name, candleType.name());
        super.rulesManager.addRule(candleType, new Affinity(risingRule, rResult));

        IRule fallingRule = new NumComparePSR(buildName(candleType, "NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT)
                .and(new NumComparePSR(buildName(candleType, "NCPSR__MA30VS05"), ma30, ma05, NumCompareFunction.GT))
                .and(new CrossPSR.FallingCrossPSR(buildName(candleType, "FCPSR_MA05VS10"), ma05, ma10)
                             .or(new CrossPSR.FallingCrossPSR(buildName(candleType, "RCPSR_MA05VS30"), ma05, ma30)))
                .overTurn(true).period(period);
        BarkRuleResult fResult = new BarkRuleResult("%s.%s的%s线MA指标走弱", super.market, super.name, candleType.name());
        super.rulesManager.addRule(candleType, new Affinity(fallingRule, fResult));
    }


    // 超买超卖信号
    protected void initWRRL(CandleType candleType, int period, double threshold) {

        CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
        PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 14);
        WRIS wr = IndicatorHelper.create_WR_IS(polarIS);
        IRule overSellRule = new ThresholdSRL(buildName(candleType, "TSRL_WR_OverSell"), wr, 100d - threshold, NumCompareFunction.GTE).overTurn(true).period(period);
        IRule overBuyRule = new ThresholdSRL(buildName(candleType, "TSRL_WR_OverBuy"), wr, threshold, NumCompareFunction.LTE).overTurn(true).period(period);

        RuleResult sResult = new BarkRuleResult("%s.%s的%s线WR指标出现超卖信号", super.market, super.name, candleType.name());
        RuleResult bResult = new RuleResult("%s.%s的%s线WR指标出现超买信号", super.market, super.name, candleType.name());
        super.rulesManager.addRule(candleType, new Affinity(overSellRule, sResult));
        super.rulesManager.addRule(candleType, new Affinity(overBuyRule, bResult));
    }
}
