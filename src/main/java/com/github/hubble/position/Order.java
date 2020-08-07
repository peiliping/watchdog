package com.github.hubble.position;


import com.github.hubble.signal.Signal;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {


    private long id;

    private double price;

    private double volume;

    private Signal signal;

    private Double expectedProfitPrice;
}
