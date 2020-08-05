package com.github.hubble.position;


import com.github.hubble.signal.Signal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Slf4j
public class PositionManager extends BasePositionManager {


    private final double unit = 0.005d;

    private TreeMap<Double, Order> orderBooks = Maps.newTreeMap();


    public PositionManager() {

        super(0.002d, 0.02d, 0.02d);
    }


    @Override
    public void handleSignal(Signal signal, double price) {

        switch (signal) {
            case BLIND:
            case CALL:
                buy(price, this.unit);
                break;
            case SHOW_HAND:
                buy(price, this.unit * 2);
                break;
            case FOLD:
                stopProfitOrders(price, 0.01d);
                break;
            case MUCK:
                stopProfitOrders(price, 0d);
                break;
        }
    }


    @Override protected boolean buy(double price, double vol) {

        if (this.orderBooks.containsKey(price)) {
            return false;
        }
        boolean r = super.buy(price, vol);
        if (r) {
            this.orderBooks.put(price, Order.builder().price(price).volume(vol).build());
        }
        return r;
    }


    @Override protected boolean sell(double price, double vol, boolean fromLowest) {

        boolean r = super.sell(price, vol, fromLowest);
        if (r) {
            while (vol > 0) {
                Map.Entry<Double, Order> entry = fromLowest ? this.orderBooks.pollFirstEntry() : this.orderBooks.pollLastEntry();
                Order tmpOrder = entry.getValue();
                if (vol >= tmpOrder.getVolume()) {
                    vol -= tmpOrder.getVolume();
                } else {
                    Order order = Order.builder().price(entry.getKey()).volume(tmpOrder.getVolume() - vol).build();
                    this.orderBooks.put(entry.getKey(), order);
                }
            }
        }
        return r;
    }


    @Override protected void stopProfitOrders(double price, double ratio) {

        final List<Order> stopProfitOrders = Lists.newArrayList();
        double totalSellVol = 0;
        double limit = price / (1 + ratio);
        for (Map.Entry<Double, Order> entry : this.orderBooks.headMap(limit, true).entrySet()) {
            stopProfitOrders.add(entry.getValue());
            totalSellVol += entry.getValue().getVolume();
        }
        if (stopProfitOrders.isEmpty()) {
            return;
        }
        sell(price, totalSellVol, true);
    }


    @Override protected void stopLossOrders(double price, double ratio) {

        final List<Order> stopLossOrders = Lists.newArrayList();
        double totalSellVol = 0;
        double limit = price / (1 - ratio);
        for (Map.Entry<Double, Order> entry : this.orderBooks.tailMap(limit, true).entrySet()) {
            stopLossOrders.add(entry.getValue());
            totalSellVol += entry.getValue().getVolume();
        }
        if (stopLossOrders.isEmpty()) {
            return;
        }
        sell(price, totalSellVol, false);
    }
}
