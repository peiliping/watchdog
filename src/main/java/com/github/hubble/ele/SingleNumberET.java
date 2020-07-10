package com.github.hubble.ele;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public class SingleNumberET extends Element {


    private double data;


    public SingleNumberET(long id, double data) {

        super(id);
        this.data = data;
    }


    @Override public boolean diff(Element other) {

        return this.data != ((SingleNumberET) other).data;
    }
}
