package com.github.hubble.ele;


public interface CustomCompare<E extends Element> {


    int compareWith(E e1, E e2);

    double delta(E e1, E e2);

    default boolean exec(E e1, E e2) {

        return compareWith(e1, e2) > 0;
    }

    CustomCompare<NumberET> numberETCompare = new CustomCompare<NumberET>() {


        @Override public int compareWith(NumberET e1, NumberET e2) {

            return Double.compare(e1.getData(), e2.getData());
        }


        @Override public double delta(NumberET e1, NumberET e2) {

            return e1.getData() - e2.getData();
        }
    };

    CustomCompare<NumberET> numberETCompareReverse = new CustomCompare<NumberET>() {


        @Override public int compareWith(NumberET e1, NumberET e2) {

            return Double.compare(e2.getData(), e1.getData());
        }


        @Override public double delta(NumberET e1, NumberET e2) {

            return e2.getData() - e1.getData();
        }
    };
}

