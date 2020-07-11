package com.github.hubble;


import com.github.hubble.ele.Element;


public interface SeriesListener<E extends Element> {


    void onChange(long seq, E ele, boolean updateOrInsert, Series<E> series);

}
