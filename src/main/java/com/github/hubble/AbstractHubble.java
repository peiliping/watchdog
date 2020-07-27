package com.github.hubble;


import com.github.hubble.common.CandleType;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.series.CandleSeriesManager;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


public abstract class AbstractHubble {


    protected String market;

    @Getter
    protected String name;

    @Getter
    protected CandleSeriesManager candleSeriesManager;

    @Getter
    protected RulesManager rulesManager;


    public AbstractHubble(String market, String name) {

        this.market = market;
        this.name = name;
        this.candleSeriesManager = new CandleSeriesManager(market, name, 128);
        this.rulesManager = new RulesManager();
    }


    public abstract AbstractHubble init();//Candle,Indicator,Rule


    protected String buildName(CandleType candleType, String key) {

        return StringUtils.joinWith(".", this.market, this.name, candleType.name(), key);
    }
}
