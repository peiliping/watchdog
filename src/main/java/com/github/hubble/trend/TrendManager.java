package com.github.hubble.trend;


import com.github.hubble.common.CandleType;
import com.github.hubble.trend.constants.Period;
import com.github.hubble.trend.constants.TrendType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class TrendManager {


    private Map<CandleType, TrendEntity> candleTypeTrendEntityMap = Maps.newHashMap();

    private Map<Period, TrendEntity> periodTrendEntityMap = Maps.newHashMap();


    public void init(CandleType candleType, Period period, TrendRule trendRule, Map<TrendType, TrendRule> degreeRules) {

        TrendEntity trendEntity = new TrendEntity(period, trendRule, degreeRules);
        this.candleTypeTrendEntityMap.put(candleType, trendEntity);
        this.periodTrendEntityMap.put(period, trendEntity);
    }


    public TrendEntity get(Period period) {

        return this.periodTrendEntityMap.get(period);
    }


    public void update(CandleType candleType) {

        TrendEntity trendEntity = this.candleTypeTrendEntityMap.get(candleType);
        trendEntity.update();
        if (trendEntity.update()) {
            log.info(trendEntity.toString());
        }
    }

}
