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

    private long inTime;

    private double inPrice;

    private Signal inSignal;

    private double volume;

    private Double targetPrice;

    private Double stopLossPrice;

    private Double maxPriceAfterPlace;

    private Double dynamicTrailingStopRatio;


    public boolean tracing(double price, double maxProfitRatio) {

        this.maxPriceAfterPlace = Math.max(this.maxPriceAfterPlace, price);

        if (this.targetPrice != null) {
            if (price >= this.targetPrice) {
                return true;
            }
        }
        if (this.stopLossPrice != null) {
            if (price <= this.stopLossPrice) {
                return true;
            }
        }
        if (this.dynamicTrailingStopRatio != null && this.maxPriceAfterPlace / this.inPrice >= (1 + this.dynamicTrailingStopRatio)) {
            if (price <= this.maxPriceAfterPlace * (1 - this.dynamicTrailingStopRatio)) {
                return true;
            }
        }
        if (price > this.inPrice * (1 + maxProfitRatio)) {
            return true;
        }
        return false;
    }


    public void completed(long outTime, double outPrice, Signal outSignal) {

        log.warn("id:{},in-time:{},in-price:{},in-signal:{},out-time:{},out-price:{},out-signal:{},vol:{},max-price:{}",
                 id, Util.timestamp2Date(inTime), inPrice, inSignal, Util.timestamp2Date(outTime), outPrice, outSignal, volume, maxPriceAfterPlace);
    }
}
