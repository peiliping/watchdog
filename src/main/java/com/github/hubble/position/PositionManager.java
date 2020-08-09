package com.github.hubble.position;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.hubble.signal.Signal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class PositionManager extends BasePositionManager {


    private final AtomicLong sequenceId;

    private final double unit = 0.005d;

    private final Map<String, Order> orderBooks = Maps.newHashMap();


    public PositionManager(String path) {

        super(0.002d, 0.02d, 0.02d);
        super.statePath = path;
        this.sequenceId = new AtomicLong(1);
    }


    @Override
    public void handleSignal(Signal signal, double price) {

        switch (signal) {
            case BLIND:
                buy(price, this.unit, null, signal);
                buy(price, this.unit, 0.01d, signal);
                break;
            case CALL:
                buy(price, this.unit, 0.01d, signal);
                break;
            case SHOW_HAND:
                //TODO
                break;
            case FOLD:
                stopProfitOrders(price, 0.008d);
                break;
            case MUCK:
                stopProfitOrders(price, -super.stopLossRatio);
                break;
        }
        saveState(price);
    }


    protected boolean buy(double price, double vol, Double expectedProfitRate, Signal signal) {

        boolean r = super.buy(price, vol);
        if (r) {
            Order od = Order.builder().id(this.sequenceId.getAndIncrement()).price(price).volume(vol).signal(signal)
                    .expectedProfitPrice(expectedProfitRate != null ? price * (1 + expectedProfitRate) : null).build();
            this.orderBooks.put(od.getId().toString(), od);
        }
        return r;
    }


    protected boolean sell(double price, Order od) {

        if (this.orderBooks.containsKey(od.getId().toString())) {
            boolean r = super.sell(price, od.getVolume());
            if (r) {
                this.orderBooks.remove(od.getId().toString());
                return true;
            }
        }
        return false;
    }


    @Override protected void stopProfitOrders(double price, double ratio) {

        final List<Order> stopOrders = Lists.newArrayList();
        double limit = price / (1 + ratio);
        for (Map.Entry<String, Order> entry : this.orderBooks.entrySet()) {
            Order od = entry.getValue();
            if (od.getPrice() <= limit) {
                stopOrders.add(od);
            } else if (od.getExpectedProfitPrice() != null) {
                if (od.getExpectedProfitPrice() <= price) {
                    stopOrders.add(od);
                }
            }
        }
        for (Order order : stopOrders) {
            sell(price, order);
        }
    }


    @Override protected void stopLossOrders(double price, double ratio) {

        final List<Order> stopOrders = Lists.newArrayList();
        double limit = price / (1 - ratio);
        for (Map.Entry<String, Order> entry : this.orderBooks.entrySet()) {
            Order od = entry.getValue();
            if (od.getPrice() >= limit) {
                stopOrders.add(od);
            }
        }
        for (Order order : stopOrders) {
            sell(price, order);
        }
    }


    @Override public JSONObject recoveryState() {

        JSONObject jsonObject = super.recoveryState();
        if (jsonObject == null) {
            return null;
        }
        this.sequenceId.set(jsonObject.getLong("sequenceId"));
        String orders = jsonObject.getString("orderBooks");
        this.orderBooks.putAll(JSON.parseObject(orders, new TypeReference<Map<String, Order>>() {


        }));
        return jsonObject;
    }


    @Override protected JSONObject snapshotState() {

        JSONObject jsonObject = super.snapshotState();
        jsonObject.put("sequenceId", this.sequenceId);
        jsonObject.put("orderBooks", this.orderBooks);
        return jsonObject;
    }
}
