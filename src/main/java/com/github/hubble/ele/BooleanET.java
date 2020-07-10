package com.github.hubble.ele;


import lombok.Getter;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public class BooleanET extends Element {


    private boolean data;


    public BooleanET(long id, boolean data) {

        super(id);
        this.data = data;
    }


    @Override public boolean diff(Element other) {

        return this.data != ((BooleanET) other).data;
    }
}
