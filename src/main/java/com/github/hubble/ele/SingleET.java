package com.github.hubble.ele;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
public class SingleET extends Element {


    private double data;


    public SingleET(long id) {

        super(id);
    }
}
