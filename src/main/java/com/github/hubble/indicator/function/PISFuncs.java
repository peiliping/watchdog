package com.github.hubble.indicator.function;


import com.github.hubble.ele.NumberET;

import java.util.function.ToDoubleBiFunction;


public class PISFuncs {


    public static final ToDoubleBiFunction<NumberET, NumberET> PERCENT = (numberET, numberET2) -> numberET.getData() / numberET2.getData() * 100;

    public static final ToDoubleBiFunction<NumberET, NumberET> SUBTRACTION = (numberET, numberET2) -> numberET.getData() - numberET2.getData();

}
