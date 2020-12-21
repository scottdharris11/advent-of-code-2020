package com.harris;

import java.util.HashMap;
import java.util.Map;

public class AdventDay15 {

    public void executePart1() {
        System.out.println("ADVENT DAY 15 - PART 1...");
        //int[] inputVals = { 0,3,6 };
        //int[] inputVals = { 1,3,2 };
        //int[] inputVals = { 2,1,3 };
        //int[] inputVals = { 1,2,3 };
        //int[] inputVals = { 2,3,1 };
        //int[] inputVals = { 3,2,1 };
        //int[] inputVals = { 3,1,2 };
        int[] inputVals = { 2,0,1,7,4,14,18 };
        int stopTurn = 2020;

        int lastNumberSpoken = determineLastNumberSpoken( inputVals, stopTurn );
        System.out.println( "  Answer: " + lastNumberSpoken );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 15 - PART 2...");
        //int[] inputVals = { 0,3,6 };
        //int[] inputVals = { 1,3,2 };
        //int[] inputVals = { 2,1,3 };
        //int[] inputVals = { 1,2,3 };
        //int[] inputVals = { 2,3,1 };
        //int[] inputVals = { 3,2,1 };
        //int[] inputVals = { 3,1,2 };
        int[] inputVals = { 2,0,1,7,4,14,18 };
        int stopTurn = 30000000;

        int lastNumberSpoken = determineLastNumberSpoken( inputVals, stopTurn );
        System.out.println( "  Answer: " + lastNumberSpoken );
    }

    private int determineLastNumberSpoken( int[] inputVals, int afterTurn ) {
        int turn = 1;
        Map<Integer,NumberTracker> trackers = new HashMap<>();
        NumberTracker lastNumberSpoken = new NumberTracker();
        while ( turn <= afterTurn ) {
            int turnNumber;
            if ( turn > inputVals.length ) {
                if ( lastNumberSpoken.timesSpoken == 1 ) {
                    turnNumber = 0;
                } else {
                    turnNumber = lastNumberSpoken.prevTurnGap;
                }
            } else {
                turnNumber = determineTurnNumber( inputVals, turn );
            }

            NumberTracker tracker = trackers.get( turnNumber );
            if ( tracker == null ) {
                tracker = new NumberTracker();
                tracker.number = turnNumber;
                trackers.put( turnNumber, tracker );
            }
            lastNumberSpoken = tracker;
            lastNumberSpoken.numberSpoken( turn );

            turn++;
        }
        return lastNumberSpoken.number;
    }

    private int determineTurnNumber( int[] inputVals, int turn ) {
        int idx = ( turn % inputVals.length ) - 1;
        if ( idx == -1 ) {
            idx = inputVals.length - 1;
        }
        return inputVals[idx];
    }

}

class NumberTracker {
    int number;
    int timesSpoken;
    int prevTurnGap;
    private int lastTurnSpoken;

    void numberSpoken( int turn ) {
        if ( timesSpoken > 0 ) {
            prevTurnGap = turn - lastTurnSpoken;
        }
        timesSpoken++;
        lastTurnSpoken = turn;
    }
}