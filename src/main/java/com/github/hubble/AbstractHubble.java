package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.hubble.position.BasePositionManager;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.series.CandleSeries;
import com.github.hubble.series.CandleSeriesManager;
import com.github.hubble.signal.SignalCallBack;
import com.github.hubble.trend.TrendManager;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;


@Getter
public abstract class AbstractHubble implements SignalCallBack {


    protected String market;

    protected String name;

    protected CandleSeriesManager candleSeriesManager;

    protected RulesManager rulesManager;

    protected TrendManager trendManager;

    protected BasePositionManager positionManager;


    public AbstractHubble(String market, String name, Set<CandleType> candleTypeSet) {

        this.market = market;
        this.name = name;
        this.candleSeriesManager = new CandleSeriesManager(128, candleTypeSet);
        this.rulesManager = new RulesManager();
        this.trendManager = new TrendManager();
    }


    public abstract AbstractHubble init();


    protected void bindPositionManager(CandleType candleType) {

        CandleSeries candleSeries = this.candleSeriesManager.getCandleSeries(candleType);
        candleSeries.bindUpsertListener(this.positionManager);
    }


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
