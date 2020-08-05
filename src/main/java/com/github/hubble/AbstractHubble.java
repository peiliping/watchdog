package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.hubble.position.PositionManager;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.series.CandleSeriesManager;
import com.github.hubble.signal.SignalCallBack;
import com.github.hubble.trend.TrendManager;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


@Getter
public abstract class AbstractHubble implements SignalCallBack {


    protected String market;

    protected String name;

    protected CandleSeriesManager candleSeriesManager;

    protected RulesManager rulesManager;

    protected TrendManager trendManager;

    protected PositionManager positionManager;


    public AbstractHubble(String market, String name) {

        this.market = market;
        this.name = name;
        this.candleSeriesManager = new CandleSeriesManager(128);
        this.rulesManager = new RulesManager();
        this.trendManager = new TrendManager();
        this.positionManager = new PositionManager();
    }


    public abstract AbstractHubble init();//Candle,Indicator,Rule


    public void addCandleETs(CandleType candleType, List<CandleET> candleETList) {

        this.candleSeriesManager.addCandleETList(candleType, candleETList);
    }


    public void addCandleET(CandleType candleType, CandleET candleET, boolean ruled) {

        this.candleSeriesManager.addCandleET(candleType, candleET);
        if (ruled) {
            this.rulesManager.traverseRules(candleType, candleET.getId());
        }
    }


    protected String buildName(CandleType candleType, String key) {

        return StringUtils.joinWith(".", this.name, candleType.name(), key);
    }
}
