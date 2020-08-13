package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.function.PISFuncs;
import com.github.hubble.indicator.general.CalculatePIS;
import com.github.hubble.indicator.general.MAIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.series.pair.CrossPSR;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.signal.Signal;
import com.github.hubble.signal.SignalRuleResult;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.task.hb.Candles;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BTC_SHOCK extends AbstractHubbleWithCommonRL {


    public BTC_SHOCK(String market, String name, String path) {

        super(market, name, Candles.candles);
        initPositionManager(new PositionManager_SHOCK(path));
    }


    @Override public AbstractHubble init() {

        initShockRL(CandleType.MIN_1, 5, 1d);
        {
            CandleType candleType = CandleType.MIN_30;
            CandleSeries candleSeries = super.candleSeriesManager.getCandleSeries(candleType);
            ToNumIS<CandleET> closeIs = IndicatorHelper.create_CLOSE_IS(candleSeries);
            MAIS ma45 = IndicatorHelper.create_MA_IS(closeIs, 45);

            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);
            ToNumIS<TernaryNumberET> bollingUp = IndicatorHelper.create_Bolling_Up_IS(bollingPIS);
            ToNumIS<TernaryNumberET> bollingDown = IndicatorHelper.create_Bolling_Down_IS(bollingPIS);

            CalculatePIS underBolling = IndicatorHelper.create_CAL_PIS("UnderBolling", closeIs, bollingDown, PISFuncs.PERCENT);
            IRule overFalling = new ThresholdSRL(buildName(candleType, "Bolling_OverFalling"), underBolling, 99.4, NumCompareFunction.LTE).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(overFalling, new SignalRuleResult("下跌过度", Signal.CALL, this)));

            IRule outRule = new CrossPSR.RisingCrossPSR(buildName(candleType, "BOLLING_DOWN_VS_MA"), bollingDown, ma45).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(outRule, new SignalRuleResult("卖出信号", Signal.MUCK, this)));

            IRule inRule = new CrossPSR.FallingCrossPSR(buildName(candleType, "BOLLING_UP_VS_MA"), bollingUp, ma45).overTurn(true).period();
            super.rulesManager.addRule(candleType, new Affinity(inRule, new SignalRuleResult("买入信号", Signal.BLIND, this)));

        }
        return this;
    }


    @Override public void spark(CandleType candleType, Signal signal, String message) {

        double currentPrice = super.candleSeriesManager.getCandleSeries(candleType).getLast().getClose();
        super.positionManager.handleSignal(signal, currentPrice);
        MsgChannel.getInstance().addResult(String.format("%s.%s(%s %s) %s", super.market, super.name, currentPrice, signal, message));
    }
}
