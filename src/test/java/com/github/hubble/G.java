package com.github.hubble;


import com.google.common.collect.Maps;

import java.util.TreeMap;


public class G {


    public static void main(String[] args) {

        TreeMap<Double, Double> xx = Maps.newTreeMap();
        xx.put(1d, 1d);
        xx.put(2d, 2d);
        xx.put(3d, 3d);
        xx.put(4d, 4d);
        xx.put(5d, 5d);
        System.out.println(xx.headMap(2d, true));

    }

}
