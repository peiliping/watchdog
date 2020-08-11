package com.github.hubble.position;


import com.github.hubble.signal.Signal;
import com.github.watchdog.common.Util;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Order {


    private long id;

    private long timeSeq;

    private double inPrice;

    private double volume;

    private Signal signal;

    private Double expectedProfitPrice;


    public void end(long endTime, double endPrice, Signal endSignal) {

        log.warn("id:{},in-time:{},in-price:{},in-signal:{},out-time:{},out-price:{},out-signal:{},vol:{}",
                 id, Util.timestamp2Date(timeSeq), inPrice, signal, Util.timestamp2Date(endTime), endPrice, endSignal, volume);
    }
}
