package com.harris;

import java.util.Collections;
import java.util.List;

public class AdventDay10 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 10 - PART 1..." );
        List<Integer> numbers = new InputReader().readIntInput( "day10-input.txt" );
        Collections.sort( numbers );
        int joltDiff1 = 0;
        int joltDiff3 = 0;
        int currJolt = 0;
        for ( int adapterJolts : numbers ) {
            if ( adapterJolts == currJolt + 1 ) {
                joltDiff1++;
            } else if ( adapterJolts == currJolt + 3 ) {
                joltDiff3++;
            }
            currJolt = adapterJolts;
        }

        joltDiff3++;

        int joltDiff = joltDiff1 *  joltDiff3;
        System.out.println( "  Jolt Diff Eval: " + joltDiff );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 10 - PART 2..." );
        List<Integer> numbers = new InputReader().readIntInput( "day10-input.txt" );
        numbers.add( 0 );
        Collections.sort( numbers );
        Integer[] adapters = numbers.toArray(new Integer[0]);

        long[] pathsToAdapters = new long[numbers.size()];
        pathsToAdapters[0] = 1;
        for ( int i = 0; i < adapters.length - 1; i++ ) {
            for ( int j = i + 1; j < adapters.length; j++ ) {
                if ( adapters[j] <= adapters[i] + 3 ) {
                    pathsToAdapters[j] += pathsToAdapters[i];
                } else {
                    break;
                }
            }
        }

        System.out.println( "  Adapter Combinations: " + pathsToAdapters[numbers.size()-1] );
    }

}
