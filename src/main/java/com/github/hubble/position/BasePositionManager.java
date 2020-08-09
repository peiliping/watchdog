package com.github.hubble.position;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.ele.CandleET;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesUpsertListener;
import com.github.hubble.signal.Signal;
import com.github.watchdog.common.Util;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public abstract class BasePositionManager implements SeriesUpsertListener<CandleET> {


    @Getter
    protected final AtomicBoolean status = new AtomicBoolean(false);

    protected final DecimalFormat formatter = new DecimalFormat("0.00000000");

    protected final double feeRatio;

    protected double cash = 1000d;

    protected double invest = 0d;

    protected double stopLossRatio;

    protected double stopProfitRatio;

    protected String statePath;


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


    protected boolean sell(double price, double vol) {

        if (check4Sell(vol)) {
            this.invest -= vol;
            this.cash += price * vol * (1 - this.feeRatio);
            log.info("sell out : {} , {} ", price, vol);
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

        if (!this.status.get()) {
            return;
        }
        stopLossOrders(ele.getClose(), this.stopLossRatio);
        stopProfitOrders(ele.getClose(), this.stopProfitRatio);
        if (!updateOrInsert) {
            saveState(ele.getClose());
        }
    }


    protected void saveState(double price) {

        log.warn("cash : {} , invest : {} , total : {}", this.formatter.format(this.cash), this.formatter.format(this.invest),
                 this.formatter.format(this.cash + this.invest * price));
        Util.writeFile(this.statePath, snapshotState().toJSONString().getBytes());
    }


    protected JSONObject snapshotState() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cash", this.cash);
        jsonObject.put("invest", this.invest);
        return jsonObject;
    }


    public JSONObject recoveryState() {

        String json = Util.readFile(this.statePath);
        log.info("loading state ." + json);
        if (StringUtils.isNotEmpty(json)) {
            JSONObject jsonObject = JSON.parseObject(json);
            this.cash = jsonObject.getDouble("cash");
            this.invest = jsonObject.getDouble("invest");
            return jsonObject;
        }
        return null;
    }
}
