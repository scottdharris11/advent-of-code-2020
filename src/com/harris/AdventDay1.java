package com.harris;

import java.util.List;

public class AdventDay1 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 1 - PART 1..." );
        int num1 = 0, num2 = 0;
        List<Integer> input = new InputReader().readIntInput( "day1-input.txt" );
        done:
        for ( int i : input ) {
            for ( int j : input ) {
                if ( i + j == 2020) {
                    num1 = i;
                    num2 = j;
                    break done;
                }
            }
        }
        long result = num1 * num2;
        System.out.println("  Found match with numbers " + num1 + " and " + num2 + ": " + result);
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 1 - PART 2..." );
        int num1 = 0, num2 = 0, num3 = 0;
        List<Integer> input = new InputReader().readIntInput( "day1-input.txt" );
        done:
        for ( int i : input ) {
            for ( int j : input ) {
                for ( int x : input ) {
                    if ( i + j + x == 2020 ) {
                        num1 = i;
                        num2 = j;
                        num3 = x;
                        break done;
                    }
                }
            }
        }
        long result = num1 * num2 * num3;
        System.out.println("  Found match with numbers " + num1 + ", " + num2 + ", and " + num3 + ": " + result);
    }

}
