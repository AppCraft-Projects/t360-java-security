package com.training360.security._06_validation;

import static java.lang.Integer.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class IntegerOverflowSolution {

    public static void main(String[] args) {
        Math.subtractExact(MIN_VALUE, 1);
        Math.addExact(MAX_VALUE, 1);
    }
}
