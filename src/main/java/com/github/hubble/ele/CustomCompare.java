package com.github.hubble.ele;


public interface CustomCompare<E extends Element> {


    int compareWith(E e1, E e2);

    default boolean result(int c) {

        return c > 0;
    }

    default boolean exec(E e1, E e2) {

        return result(compareWith(e1, e2));
    }
}
