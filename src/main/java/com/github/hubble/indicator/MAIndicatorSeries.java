package com.github.hubble.indicator;


import com.github.hubble.ele.CandleET;
import com.github.hubble.ele.SingleET;


public class MAIndicatorSeries extends CacheIndicatorSeries<SingleET> {


    private double last;


    public MAIndicatorSeries(String name, int size, long interval, int step) {

        super(name, size, interval, step);
    }


    @Override public void onChange(CandleET ele, boolean replace) {

        SingleET newSingle = new SingleET(ele.getId(), ele.getClose());

        if (super.cache.getList().size() < super.step) {
            super.cache.add(newSingle);
            if (super.cache.getList().size() == super.step) {
                this.last = super.cache.getList().stream().mapToDouble(value -> value.getData()).sum();
                add(new SingleET(newSingle.getId(), this.last / super.step));
            }
            return;
        }

        this.last -= (replace ? super.cache.getLast().getData() : super.cache.getFirst().getData());
        this.last += newSingle.getData();
        super.cache.add(newSingle);
        add(new SingleET(newSingle.getId(), this.last / super.step));
    }
}
