package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.AbstractHubbleWithCommonRL;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.MAIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.NumberComparePSR;
import com.github.hubble.series.CandleSeries;
import com.github.watchdog.common.BarkRuleResult;


public class BTC extends AbstractHubbleWithCommonRL {


    public BTC(String market, String name) {

        super(market, name);
        super.shockRatio = 0.8d;
    }


    @Override public AbstractHubble init() {

        initShockSRL();

        {
            CandleSeries min60_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.MIN_60);
        }
        {
            CandleSeries day_CS = super.candleSeriesManager.getOrCreateCandleSeries(CandleType.DAY);
            ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(day_CS);

            MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
            MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);
            MAIS ma30 = IndicatorHelper.create_MA_IS(closeSeries, 30);

            IRule risingRule = new NumberComparePSR(buildName(CandleType.DAY, "NCPSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT)
                    .and(new NumberComparePSR(buildName(CandleType.DAY, "NCPSR_MA10VS30"), ma10, ma30, NumCompareFunction.GT)).overTurn(true).period(3600);
            BarkRuleResult rResult = new BarkRuleResult("%s.%s的日K线MA指标呈现多头排列上升趋势", super.market, super.name);
            super.rulesManager.addRule(CandleType.DAY, new Affinity(risingRule, rResult));

            IRule fallingRule = new NumberComparePSR(buildName(CandleType.DAY, "NCPSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT)
                    .and(new NumberComparePSR(buildName(CandleType.DAY, "NCPSR__MA30VS05"), ma30, ma05, NumCompareFunction.GT)).overTurn(true).period(3600);
            BarkRuleResult fResult = new BarkRuleResult("%s.%s的日K线MA指标呈现多头排列下跌趋势", super.market, super.name);
            super.rulesManager.addRule(CandleType.DAY, new Affinity(fallingRule, fResult));
        }
        return this;
    }
}
