package com.github.hubble.series;


import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class SeriesParams {


    private String name;

    private int size;

    private long interval;


    public SeriesParams createNew(String name) {

        return SeriesParams.builder().name(name).size(this.size).interval(this.interval).build();
    }

}
