package com.github.hubble.position;


import com.github.hubble.signal.Signal;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Order {


    private Long id;

    private long timeSequence;

    private double price;

    private double volume;

    private Signal signal;

    private Double expectedProfitPrice;


    public void end(long endTime, double endPrice) {

        log.warn("id : {} , in-time : {} , in : {} ,out-time: {} , out : {} , vol : {} , signal : {}", id, timeSequence, price, endTime, endPrice, volume, signal);
    }
}
