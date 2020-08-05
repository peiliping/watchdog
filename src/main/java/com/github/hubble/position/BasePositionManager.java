package com.github.hubble.position;


import com.github.hubble.ele.CandleET;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesUpsertListener;
import com.github.hubble.signal.Signal;
import com.github.watchdog.common.Util;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class BasePositionManager implements SeriesUpsertListener<CandleET> {


    protected final long miniId = Util.nowSec() / 60 * 60;

    protected final double feeRatio;

    protected double cash = 1000d;

    protected double invest = 0d;

    protected double stopLossRatio;

    protected double stopProfitRatio;


    public BasePositionManager(double feeRatio, double stopLossRatio, double stopProfitRatio) {

        this.feeRatio = feeRatio;
        this.stopLossRatio = stopLossRatio;
        this.stopProfitRatio = stopProfitRatio;
    }


    public abstract void handleSignal(Signal signal, double price);


    protected boolean buy(double price, double vol) {

        if (check4Buy(price, vol)) {
            this.invest += vol;
            this.cash -= price * vol * (1 + this.feeRatio);
            log.info("buy in : {} , {} ", price, vol);
            return true;
        }
        return false;
    }


    protected boolean check4Buy(double price, double vol) {

        double k = price * vol * (1 + this.feeRatio);
        return this.cash >= k;
    }


    protected boolean sell(double price, double vol, boolean fromLowest) {

        if (check4Sell(vol)) {
            this.invest -= vol;
            this.cash += price * vol * (1 - this.feeRatio);
            log.info("sell out : {} , {} , {} ", price, vol, fromLowest);
            return true;
        }
        return false;
    }


    protected boolean check4Sell(double vol) {

        return this.invest >= vol;
    }


    protected abstract void stopProfitOrders(double price, double ratio);


    protected abstract void stopLossOrders(double price, double ratio);


    @Override public void onChange(long seq, CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        if (ele.getId() < this.miniId) {
            return;
        }
        stopLossOrders(ele.getClose(), this.stopLossRatio);
        stopProfitOrders(ele.getClose(), this.stopProfitRatio);
        if (!updateOrInsert) {
            log.info("cash : {} , invest : {} , total : {}", this.cash, this.invest, this.cash + this.invest * ele.getClose());
        }
    }
}
