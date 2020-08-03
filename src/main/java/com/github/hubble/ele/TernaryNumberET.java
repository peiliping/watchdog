package com.github.hubble.ele;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public class TernaryNumberET extends Element {


    private final double first;

    private final double second;

    private final double third;


    public TernaryNumberET(long id, double first, double second, double third) {

        super(id);
        this.first = first;
        this.second = second;
        this.third = third;
    }


    @Override public boolean diff(Element other) {

        TernaryNumberET that = (TernaryNumberET) other;
        return this.first != that.first
               || this.second != that.second
               || this.third != that.third;
    }


    public boolean isInBox(double t) {

        return this.third <= t && this.first >= t;
    }
}
