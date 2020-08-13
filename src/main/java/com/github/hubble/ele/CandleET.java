package com.github.hubble.ele;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@ToString(callSuper = true)
public class CandleET extends Element {


    private final double open;

    private final double low;

    private final double high;

    private final double close;

    private final double amount;//成交量

    private final double volume; //成交额

    private final int count; //成交笔数


    public CandleET(long id, double open, double low, double high, double close, double amount, double volume, int count) {

        super(id);
        this.open = open;
        this.low = low;
        this.high = high;
        this.close = close;
        this.amount = amount;
        this.volume = volume;
        this.count = count;
    }


    @Override public boolean diff(Element other) {

        CandleET that = (CandleET) other;
        return this.open != that.open
               || this.low != that.low
               || this.high != that.high
               || this.close != that.close
               || this.amount != that.amount
               || this.volume != that.volume
               || this.count != that.count;
    }


    public List<CandleET> split4BackTest() {

        List<CandleET> olhc = Lists.newArrayList();
        olhc.add(new CandleET(this.id, this.open, this.open, this.open, this.open, this.amount, this.volume, this.count));
        double t = (this.low + this.high) / 2;
        if (this.close > this.open) {
            olhc.add(new CandleET(this.id, this.open, this.low, this.open, this.low, this.amount, this.volume, this.count));
            olhc.add(new CandleET(this.id, this.open, this.low, Math.max(this.open, t), t, this.amount, this.volume, this.count));
            olhc.add(new CandleET(this.id, this.open, this.low, this.high, this.high, this.amount, this.volume, this.count));
        } else {
            olhc.add(new CandleET(this.id, this.open, this.open, this.high, this.high, this.amount, this.volume, this.count));
            olhc.add(new CandleET(this.id, this.open, this.low, this.high, t, this.amount, this.volume, this.count));
            olhc.add(new CandleET(this.id, this.open, this.low, this.high, this.low, this.amount, this.volume, this.count));
        }
        olhc.add(this);
        return olhc;
    }
}

