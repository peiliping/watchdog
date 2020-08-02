package com.github.hubble.trend.constants;


public enum TrendDegree {

    POSITIVE("P"),
    UNCERTAIN("UN"),
    NEGATIVE("N");

    public String name;


    private TrendDegree(String n) {

        this.name = n;
    }
}
