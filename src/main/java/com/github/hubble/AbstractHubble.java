package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.series.CandleSeriesManager;
import com.github.hubble.trend.TrendManager;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


@Getter
public abstract class AbstractHubble {


    protected String market;

    protected String name;

    protected CandleSeriesManager candleSeriesManager;

    protected RulesManager rulesManager;

    protected TrendManager trendManager;


    public AbstractHubble(String market, String name) {

        this.market = market;
        this.name = name;
        this.candleSeriesManager = new CandleSeriesManager(market, name, 128);
        this.rulesManager = new RulesManager();
        this.trendManager = new TrendManager();
    }


    public abstract AbstractHubble init();//Candle,Indicator,Rule


    protected String buildName(CandleType candleType, String key) {

        return StringUtils.joinWith(".", this.name, candleType.name(), key);
    }
}
