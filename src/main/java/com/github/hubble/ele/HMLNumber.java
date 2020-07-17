package com.github.hubble.ele;


public class HMLNumber extends Element {


    private double high;

    private double middle;

    private double low;


    public HMLNumber(long id, double high, double middle, double low) {

        super(id);
        this.high = high;
        this.middle = middle;
        this.low = low;
    }


    @Override public boolean diff(Element other) {

        HMLNumber that = (HMLNumber) other;
        return this.high != that.high
               || this.middle != that.middle
               || this.low != that.low;
    }
}
