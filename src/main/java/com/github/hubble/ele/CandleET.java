package com.github.hubble.ele;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
public class CandleET extends Element {


    private double open;

    private double low;

    private double high;

    private double close;

    private double amount;//成交量

    private double volume; //成交额

    private int count; //成交笔数


    public CandleET(long id) {

        super(id);
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
}

