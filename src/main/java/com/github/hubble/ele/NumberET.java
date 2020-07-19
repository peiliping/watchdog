package com.github.hubble.ele;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public class NumberET extends Element {


    private final double data;


    public NumberET(long id, double data) {

        super(id);
        this.data = data;
    }


    @Override public boolean diff(Element other) {

        return this.data != ((NumberET) other).data;
    }
}
