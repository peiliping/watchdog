package com.github.hubble.position;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class Order {


    private long id;

    private double price;

    private double volume;

    private Double expectedProfitPrice;
}
