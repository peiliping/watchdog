package com.github.hubble.position;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class Order {


    private double price;

    private double volume;

}
