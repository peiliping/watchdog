package com.github.hubble;


import com.google.common.collect.Maps;

import java.util.Map;


public class MapQueue<E> {


    private final Map<Integer, Container<E>> map = Maps.newConcurrentMap();


    public void add(Integer id, E e) {

        Container<E> c = this.map.get(id);
        if (c == null) {
            c = new Container<>();
            Container<E> o = this.map.putIfAbsent(id, c);
            if (o != null) {
                c = o;
            }
            c.add(e);
        }
    }

}
