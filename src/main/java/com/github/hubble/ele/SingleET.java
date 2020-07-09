package com.github.hubble.ele;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public class SingleET extends Element {


    private double data;


    public SingleET(long id, double data) {

        super(id);
        this.data = data;
    }
}
