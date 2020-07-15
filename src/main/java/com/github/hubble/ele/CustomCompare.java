package com.github.hubble.ele;


public interface CustomCompare<E extends Element> {


    int compareWith(E e1, E e2);

    double delta(E e1, E e2);

    default boolean result(int c) {

        return c > 0;
    }

    default boolean exec(E e1, E e2) {

        return result(compareWith(e1, e2));
    }

    CustomCompare<NumberET> numberETCompare = new CustomCompare<NumberET>() {


        @Override public int compareWith(NumberET e1, NumberET e2) {

            return Double.compare(e1.getData(), e2.getData());
        }


        @Override public double delta(NumberET e1, NumberET e2) {

            return e1.getData() - e2.getData();
        }
    };
}
