package com.github.hubble.trend.constants;


public enum Period {

    SHORT("S"),
    MEDIUM("M"),
    LONG("L");

    public String name;


    private Period(String n) {

        this.name = n;
    }
}
