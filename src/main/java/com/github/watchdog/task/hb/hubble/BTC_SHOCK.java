package com.github.watchdog.task.hb.hubble;


import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.common.NumCompareFunction;
import com.github.hubble.ele.TernaryNumberET;
import com.github.hubble.indicator.IndicatorHelper;
import com.github.hubble.indicator.general.DeltaIS;
import com.github.hubble.indicator.general.ToNumIS;
import com.github.hubble.indicator.specific.BollingBandWidthIS;
import com.github.hubble.indicator.specific.BollingPIS;
import com.github.hubble.rule.Affinity;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.series.DirectionalSRL;
import com.github.hubble.rule.series.threshold.ThresholdSRL;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.signal.Signal;
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
        //长期趋势
        {
            CandleType candleType = CandleType.HOUR_4;
            CandleSeries candleSeries = super.candleSeriesManager.getCandleSeries(candleType);

            BollingPIS bollingPIS = IndicatorHelper.create_Bolling_PIS(candleSeries, 20);
            ToNumIS<TernaryNumberET> bollingUp = IndicatorHelper.create_Bolling_Up_IS(bollingPIS);
            ToNumIS<TernaryNumberET> bollingMiddle = IndicatorHelper.create_Bolling_Middler_IS(bollingPIS);
            ToNumIS<TernaryNumberET> bollingDown = IndicatorHelper.create_Bolling_Down_IS(bollingPIS);

            BollingBandWidthIS bollingBand = IndicatorHelper.create_Bolling_Band_Width_IS(bollingPIS);
            DeltaIS bbDeltaIS = IndicatorHelper.create_Delta_IS(bollingBand);

            IRule bUpRising = new DirectionalSRL.UpDSRL(buildName(candleType, "B_UP_Rising"), bollingUp, 3);
            IRule bMdlRising = new DirectionalSRL.UpDSRL(buildName(candleType, "B_MDL_Rising"), bollingMiddle, 3);
            IRule bDownFalling = new DirectionalSRL.DownDSRL(buildName(candleType, "B_DOWN_Rising"), bollingDown, 3);
            IRule extendRule = new ThresholdSRL(buildName(candleType, "BB_Extend"), bbDeltaIS, 0d, 4, NumCompareFunction.GTE);
            IRule x = extendRule.and(bUpRising).and(bMdlRising).and(bDownFalling).overTurn(true).period();

            super.rulesManager.addRule(candleType, new Affinity(x, new RuleResult("BB上涨")));
        }
        return this;
    }


    @Override public void spark(CandleType candleType, Signal signal, String message) {

        double currentPrice = super.candleSeriesManager.getCandleSeries(candleType).getLast().getClose();
        super.positionManager.handleSignal(signal, currentPrice);
        MsgChannel.getInstance().addResult(String.format("%s.%s(%s %s) %s", super.market, super.name, currentPrice, signal, message));
    }
}
