package com.github.hubble.position;


import com.github.hubble.ele.CandleET;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesUpsertListener;
import com.github.hubble.signal.OperateSignal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class PositionManager implements SeriesUpsertListener<CandleET> {


    private final double unit = 0.005d;

    private final double feeRatio = 0.002d;

    private final double stopLossRatio = 0.02;

    private double cash = 1000d;

    private double invest = 0d;

    private TreeMap<Double, Order> orderBooks = Maps.newTreeMap();


    public void handleSignal(OperateSignal signal, double price) {

        if (OperateSignal.INPUT == signal && checkForBuy(1)) {
            if (this.orderBooks.containsKey(price)) {
                return;
            }
            buy(price, this.unit);
        } else if (OperateSignal.OUTPUT == signal && checkForSell(1)) {
            double k = stopProfit(price);
            if (k > 0) {
                sell(price, k, true);
            }
        }
    }


    private void buy(double price, double vol) {

        this.invest += vol;
        this.cash -= price * vol * (1 + this.feeRatio);
        this.orderBooks.put(price, Order.builder().price(price).volume(vol).build());
    }


    private void sell(double price, double vol, boolean fromLowest) {

        this.invest -= vol;
        this.cash += price * vol * (1 - this.feeRatio);
        while (vol > 0) {
            Map.Entry<Double, Order> entry = fromLowest ? this.orderBooks.pollFirstEntry() : this.orderBooks.pollLastEntry();
            Order tmpOrder = entry.getValue();
            if (vol >= tmpOrder.getVolume()) {
                vol -= tmpOrder.getVolume();
            } else {
                this.orderBooks.put(entry.getKey(), Order.builder().price(entry.getKey()).volume(tmpOrder.getVolume() - vol).build());
            }
        }
    }


    private boolean checkForSell(int ratio) {

        return this.invest >= this.unit * ratio;
    }


    private boolean checkForBuy(int ratio) {

        if (this.cash < 100d) {
            return false;
        }
        double k = this.unit * ratio * (1 + this.feeRatio);
        if (this.cash < k) {
            return false;
        }
        return true;
    }


    private double stopProfit(double price) {

        final List<Order> stopProfitOrders = Lists.newArrayList();
        double limit = price / (1 + this.feeRatio * 2.5);
        for (Map.Entry<Double, Order> entry : this.orderBooks.headMap(limit, true).entrySet()) {
            stopProfitOrders.add(entry.getValue());
        }
        if (stopProfitOrders.isEmpty()) {
            return 0d;
        }
        return stopProfitOrders.stream().mapToDouble(Order::getVolume).sum();
    }


    @Override public void onChange(long seq, CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        final List<Order> stopLossOrders = Lists.newArrayList();
        double limit = ele.getClose() / (1 - this.stopLossRatio);
        for (Map.Entry<Double, Order> entry : this.orderBooks.tailMap(limit, true).entrySet()) {
            stopLossOrders.add(entry.getValue());
        }
        for (Order order : stopLossOrders) {
            sell(ele.getClose(), order.getVolume(), false);
        }
    }
}
