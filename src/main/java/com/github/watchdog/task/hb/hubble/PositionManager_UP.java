package com.github.watchdog.task.hb.hubble;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.hubble.position.BasePositionManager;
import com.github.hubble.position.Order;
import com.github.hubble.signal.Signal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class PositionManager_UP extends BasePositionManager {


    private final AtomicLong sequenceId = new AtomicLong(1);

    private final Map<Long, Order> orderBooks = Maps.newHashMap();

    private final double unit = 0.01d;

    private final double maxStopProfitRatio = 0.05d;

    private final double dynamicTrailingStopRatio = 0.02d;

    private final double maxStopLossRatio = 0.025d;


    public PositionManager_UP(String path) {

        super(1000d, 0d, 0.002d, path);
    }


    @Override
    public void handleSignal(Signal signal, double price) {

        switch (signal) {
            case BLIND:
                buy(Order.builder().id(this.sequenceId.getAndIncrement())
                            .inTime(this.clock.get())
                            .inPrice(price)
                            .inSignal(signal)
                            .volume(this.unit)
                            .stopLossPrice(price * (1 - this.maxStopLossRatio))
                            .maxPriceAfterPlace(price)
                            .dynamicTrailingStopRatio(this.dynamicTrailingStopRatio)
                            .build());
                break;
            case CALL:
                buy(Order.builder().id(this.sequenceId.getAndIncrement())
                            .inTime(this.clock.get())
                            .inPrice(price)
                            .inSignal(signal)
                            .volume(this.unit)
                            .targetPrice(price * 1.015d)
                            .stopLossPrice(price * (1 - this.maxStopLossRatio))
                            .maxPriceAfterPlace(price)
                            .build());
                break;
            case SHOW_HAND:
                //TODO
                break;
            case FOLD:
                stopProfitOrders(Signal.FOLD, price, this.maxStopLossRatio);
                break;
            case MUCK:
                stopProfitOrders(Signal.MUCK, price, -this.maxStopLossRatio);
                break;
        }
        saveState(price);
    }


    @Override protected void tracing(double price) {

        final List<Order> stopOrders = Lists.newArrayList();
        for (Map.Entry<Long, Order> entry : this.orderBooks.entrySet()) {
            Order od = entry.getValue();
            if (od.tracing(price, this.maxStopProfitRatio)) {
                stopOrders.add(od);
            }
        }
        for (Order order : stopOrders) {
            sell(Signal.NONE, price, order);
        }
    }


    private boolean buy(Order od) {

        boolean r = super.buy(od.getInPrice(), od.getVolume());
        if (r) {
            this.orderBooks.put(od.getId(), od);
        }
        return r;
    }


    private boolean sell(Signal signal, double price, Order od) {

        if (this.orderBooks.containsKey(od.getId())) {
            boolean r = super.sell(price, od.getVolume());
            if (r) {
                od.completed(clock.get(), price, signal);
                this.orderBooks.remove(od.getId());
                return true;
            }
        }
        return false;
    }


    private void sell(Signal signal, double price, List<Order> orders) {

        for (Order order : orders) {
            sell(signal, price, order);
        }
    }


    private void stopLossOrders(Signal signal, double price, double ratio) {

        final List<Order> stopOrders = Lists.newArrayList();
        double limit = price / (1 - ratio);
        for (Map.Entry<Long, Order> entry : this.orderBooks.entrySet()) {
            Order od = entry.getValue();
            if (od.getInPrice() >= limit) {
                stopOrders.add(od);
            }
        }
        sell(signal, price, stopOrders);
    }


    private void stopProfitOrders(Signal signal, double price, double ratio) {

        final List<Order> stopOrders = Lists.newArrayList();
        double limit = price / (1 + ratio);
        for (Map.Entry<Long, Order> entry : this.orderBooks.entrySet()) {
            Order od = entry.getValue();
            if (od.getInPrice() <= limit) {
                stopOrders.add(od);
            }
        }
        sell(signal, price, stopOrders);
    }


    @Override public JSONObject recoveryState() {

        JSONObject jsonObject = super.recoveryState();
        if (jsonObject == null) {
            return null;
        }
        this.sequenceId.set(jsonObject.getLong("sequenceId"));
        String orders = jsonObject.getString("orderBooks");
        this.orderBooks.putAll(JSON.parseObject(orders, new TypeReference<Map<Long, Order>>() {


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
