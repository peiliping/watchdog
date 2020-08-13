package com.github.hubble.position;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.common.Clock;
import com.github.hubble.ele.CandleET;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesUpsertListener;
import com.github.hubble.signal.Signal;
import com.github.watchdog.common.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public abstract class BasePositionManager implements SeriesUpsertListener<CandleET> {


    protected final AtomicBoolean status = new AtomicBoolean(false);

    protected final DecimalFormat formatter = new DecimalFormat("0.00000000");

    protected final Clock clock = new Clock();

    protected final double feeRatio;

    protected final String statePath;

    protected double cash;

    protected double invest;


    public BasePositionManager(double cash, double invest, double feeRatio, String statePath) {

        this.feeRatio = feeRatio;
        this.cash = cash;
        this.invest = invest;
        this.statePath = statePath;
    }


    public void open() {

        this.status.set(true);
    }


    public void close() {

        this.status.set(false);
    }


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
            this.invest = Double.valueOf(this.formatter.format(this.invest));
            this.cash += price * vol * (1 - this.feeRatio);
            log.info("sell out : {} , {} ", price, vol);
            return true;
        }
        return false;
    }


    protected boolean check4Sell(double vol) {

        return this.invest >= vol;
    }


    public abstract void handleSignal(Signal signal, double price);

    protected abstract void tracing(double price);


    @Override public void onChange(long seq, CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        this.clock.update(ele.getId());
        if (!this.status.get()) {
            return;
        }
        tracing(ele.getClose());
        if (!updateOrInsert) {
            saveState(ele.getClose());
        }
    }


    protected void saveState(double price) {

        log.warn("time : {} , cash : {} , invest : {} , total : {}", this.clock.get(), this.formatter.format(this.cash),
                 this.formatter.format(this.invest), this.formatter.format(this.cash + this.invest * price));
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
