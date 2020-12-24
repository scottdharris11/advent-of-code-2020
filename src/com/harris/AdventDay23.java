package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventDay23 {

    public void executePart1() {
        System.out.println("ADVENT DAY 23 - PART 1...");
        //List<Integer> cups = new ArrayList( Arrays.asList( 3,8,9,1,2,5,4,6,7 ) );
        List<Integer> cups = new ArrayList( Arrays.asList( 6,8,5,9,7,4,2,1,3 ) );

        Instant start = Instant.now();
        int currentCup = 0;
        for ( int i = 0; i < 100; i++ ) {
            currentCup = playRound( currentCup, cups );
        }

        StringBuilder sb = new StringBuilder();
        int idxOfOne = cups.indexOf( 1 );
        for ( int i = idxOfOne + 1; i < cups.size(); i++ ) {
            sb.append( cups.get( i ) );
        }
        for ( int i = 0; i < idxOfOne; i++ ) {
            sb.append( cups.get( i ) );
        }

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + sb.toString() );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 23 - PART 2...");
        List<Integer> cups = new ArrayList( Arrays.asList( 3,8,9,1,2,5,4,6,7 ) );
        //List<Integer> cups = new ArrayList( Arrays.asList( 6,8,5,9,7,4,2,1,3 ) );

        Instant start = Instant.now();
        for ( int i = 10; i <= 100; i++ ) {
            cups.add( i );
        }

        int currentCup = 0;
        for ( int i = 0; i < 100; i++ ) {
            if ( i % 1000 == 0 ) { System.out.println( "  Playing round: " + (i+1) ); }
            currentCup = playRound( currentCup, cups );
        }

        int idxOfOne = cups.indexOf( 1 );
        int firstValIdx = idxOfOne + 1;
        int firstVal = firstValIdx < cups.size() ? cups.get( firstValIdx ) : cups.get( firstValIdx - cups.size() );
        int secondValIdx = idxOfOne + 2;
        int secondVal = secondValIdx < cups.size() ? cups.get( secondValIdx ) : cups.get( secondValIdx - cups.size() );
        long answer = firstVal * secondVal;

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] WRONG Answer: " + answer );
    }

    private int playRound( int currentCup, List<Integer> cups ) {
        // Get the current cup value
        int currentCupLbl = cups.get( currentCup );

        // Remove three cards left of current cup
        List<Integer> removed = new ArrayList<>();
        int removeIdx = currentCup + 1;
        for ( int i = 0; i < 3; i++ ) {
            if ( removeIdx >= cups.size() ) {
                removeIdx = 0;
            }
            removed.add( cups.remove( removeIdx ) );
        }

        // Find the destination index (current cup label - 1 or next in line)
        int destinationLbl = currentCupLbl - 1;
        int destinationIdx = -1;
        while ( destinationIdx < 0 ) {
            int minLbl = Integer.MAX_VALUE;
            int maxLbl = Integer.MIN_VALUE;
            for ( int i = 0; i < cups.size(); i++ ) {
                int cupLbl = cups.get( i );
                if ( cupLbl < minLbl ) { minLbl = cupLbl; }
                if ( cupLbl > maxLbl ) { maxLbl = cupLbl; }
                if ( cupLbl == destinationLbl ) {
                    destinationIdx = i;
                    break;
                }
            }

            if ( destinationIdx < 0 ) {
                destinationLbl--;
                if ( destinationLbl < minLbl ) {
                    destinationLbl = maxLbl;
                }
            }
        }

        // place cups back in the list at the destination index
        destinationIdx++;
        for ( Integer r : removed ) {
            cups.add( destinationIdx, r );
            destinationIdx++;
        }

        // Return index of next cup
        int nextCup = cups.indexOf( currentCupLbl ) + 1;
        if ( nextCup >= cups.size() ) {
            nextCup = 0;
        }
        return nextCup;
    }

}
