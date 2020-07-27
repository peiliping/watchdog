package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.PolarIS;
import com.github.hubble.indicator.general.WRIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.watchdog.common.BarkRuleResult;


public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 0.8d;
    }


    @Override public AbstractHubble init() {

        initShockRL();
        initMARL(CandleType.MIN_60, 1800);
        {
            CandleType candleType = CandleType.MIN_60;
            CandleSeries candleSeries = super.candleSeriesManager.getOrCreateCandleSeries(candleType);
            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(candleSeries, 14);
            WRIS wr = IndicatorHelper.create_WR_IS(polarIS);
            IRule overSellRule = new ThresholdSRL(buildName(candleType, "TSRL_WR_OverSell"), wr, 97, NumCompareFunction.GTE).overTurn(true).period(1800);
            IRule overBuyRule = new ThresholdSRL(buildName(candleType, "TSRL_WR_OverBuy"), wr, 3, NumCompareFunction.LTE).overTurn(true).period(1800);

            BarkRuleResult sResult = new BarkRuleResult("%s.%s的%s线WR指标出现超卖信号", super.market, super.name, candleType.name());
            BarkRuleResult bResult = new BarkRuleResult("%s.%s的%s线WR指标出现超买信号", super.market, super.name, candleType.name());
            super.rulesManager.addRule(candleType, new Affinity(overSellRule, sResult));
            super.rulesManager.addRule(candleType, new Affinity(overBuyRule, bResult));
        }
        initMARL(CandleType.DAY, 3600);
        return this;
    }
}
