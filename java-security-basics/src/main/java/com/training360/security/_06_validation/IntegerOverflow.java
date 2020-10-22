package com.training360.security._06_validation;

import static java.lang.Integer.*;

@SuppressWarnings("NumericOverflow")
public class IntegerOverflow {

    public static void main(String[] args) {
        System.out.printf("MIN_VALUE-1 = %d%n", (MIN_VALUE - 1));                      // max value
        System.out.printf("MAX_VALUE+1 = %d%n", (MAX_VALUE + 1));                      // min value
        System.out.printf("MAX_VALUE+1(u) = %s%n", toUnsignedString(MAX_VALUE + 1)); // max value +1
        System.out.printf("-1(u) = %d%n", toUnsignedLong(-1));                      // ???
        System.out.printf("abs(MIN_VALUE)= %d%n", Math.abs(MIN_VALUE));                // why???
    }
}
