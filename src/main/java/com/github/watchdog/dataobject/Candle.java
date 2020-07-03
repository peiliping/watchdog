package com.github.watchdog.dataobject;


import lombok.*;
import org.jetbrains.annotations.NotNull;


@Getter
@Builder
@ToString
public class Candle implements Comparable<Candle> {


    private long id;

    private double open;

    private double high;

    private double low;

    private double close;

    private double vol; //成交量


    @Override public int compareTo(@NotNull Candle that) {

        return Long.compare(this.id, that.id);
    }
}

