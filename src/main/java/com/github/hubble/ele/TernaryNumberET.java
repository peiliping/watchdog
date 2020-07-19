package com.github.hubble.ele;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public class TernaryNumberET extends Element {


    private final double high;

    private final double middle;

    private final double low;


    public TernaryNumberET(long id, double high, double middle, double low) {

        super(id);
        this.high = high;
        this.middle = middle;
        this.low = low;
    }


    @Override public boolean diff(Element other) {

        TernaryNumberET that = (TernaryNumberET) other;
        return this.high != that.high
               || this.middle != that.middle
               || this.low != that.low;
    }
}
