package com.github.hubble.series;


import com.github.hubble.common.CandleType;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class SeriesParams {


    private String name;

    private int size;

    private CandleType candleType;


    public SeriesParams createNew(String name) {

        return SeriesParams.builder().name(name).size(this.size).candleType(this.candleType).build();
    }

}
