package com.github.watchdog.task.hb;


public class Symbol {

    //            ToNumIS<CandleET> closeSeries = IndicatorHelper.create_CLOSE_IS(series);
    //            PolarIS polarIS = IndicatorHelper.create_POLAR_IS(series, 14);
    //
    //            MAIS ma05 = IndicatorHelper.create_MA_IS(closeSeries, 5);
    //            MAIS ma10 = IndicatorHelper.create_MA_IS(closeSeries, 10);
    //            MAIS ma30 = IndicatorHelper.create_MA_IS(closeSeries, 30);
    //
    //            BollingPIS bolling = IndicatorHelper.create_Bolling_PIS(closeSeries);
    //            MACDPIS macd = IndicatorHelper.create_MACD_PIS(closeSeries);
    //            RSIPIS rsi = IndicatorHelper.create_RSI_PIS(closeSeries);
    //
    //            KDJPIS kdj = IndicatorHelper.create_KDJ_PIS(polarIS);
    //            WRIS wr = IndicatorHelper.create_WR_IS(polarIS);
    //
    //            IRule risingRule = new NumberComparePSR(buildName("CSR_MA05VS10"), ma05, ma10, NumCompareFunction.GT)
    //                    .and(new NumberComparePSR(buildName("CSR_MA10VS30"), ma10, ma30, NumCompareFunction.GT)).overTurn(true);
    //            IRule fallingRule = new NumberComparePSR(buildName("CSR_MA10VS05"), ma10, ma05, NumCompareFunction.GT)
    //                    .and(new NumberComparePSR(buildName("CSR_MA30VS10"), ma30, ma10, NumCompareFunction.GT)).overTurn(true);
    //            addRule(candleType, risingRule.alternateRule(fallingRule), new BarkRuleResult(buildName(" MA趋势走强")));
    //            addRule(candleType, fallingRule.alternateRule(risingRule), new BarkRuleResult(buildName(" MA趋势走弱")));
    //
    //            IRule overSellRule = new ThresholdSRL(buildName("TSR_WR_OS"), wr, 95, NumCompareFunction.GTE).overTurn(true);
    //            IRule overBuyRule = new ThresholdSRL(buildName("TSR_WR_OB"), wr, 5, NumCompareFunction.LTE).overTurn(true);
    //            addRule(candleType, overSellRule.alternateRule(overBuyRule), new BarkRuleResult(buildName(" WR提示超卖")));
    //            addRule(candleType, overBuyRule.alternateRule(overSellRule), new BarkRuleResult(buildName(" WR提示超买")));

}
