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
        olhc.add(new CandleET(this.getId(), this.getOpen(), this.getOpen(), this.getOpen(), this.getOpen(), this.getAmount(), this.getVolume(), this.getCount()));
        if (this.getClose() > this.getOpen()) {
            olhc.add(new CandleET(this.getId(), this.getOpen(), this.getLow(), this.getOpen(), this.getLow(), this.getAmount(), this.getVolume(), this.getCount()));
            olhc.add(new CandleET(this.getId(), this.getOpen(), this.getLow(), this.getHigh(), this.getHigh(), this.getAmount(), this.getVolume(), this.getCount()));
        } else {
            olhc.add(new CandleET(this.getId(), this.getOpen(), this.getOpen(), this.getHigh(), this.getHigh(), this.getAmount(), this.getVolume(), this.getCount()));
            olhc.add(new CandleET(this.getId(), this.getOpen(), this.getLow(), this.getHigh(), this.getLow(), this.getAmount(), this.getVolume(), this.getCount()));
        }
        olhc.add(this);
        return olhc;
    }
}

