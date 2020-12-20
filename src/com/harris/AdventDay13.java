package com.harris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdventDay13 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 13 - PART 1..." );
        //List<String> lines = Arrays.asList( "939", "7,13,x,x,59,x,31,19" );
        List<String> lines = new InputReader().readStringInput( "day13-input.txt" );

        long possibleDeparture = Long.parseLong( lines.get(0) );
        List<Bus> buses = Arrays.stream( lines.get(1).split( "," ) ).filter( x -> ! x.equals( "x" ) ).map( x -> {
            Bus bus = new Bus();
            bus.loopTime = Integer.parseInt( x );
            return bus;
        }).collect( Collectors.toList() );

        long waitTime = Long.MAX_VALUE;
        int waitBusId = 0;
        for ( Bus bus : buses ) {
            long busWaitTime = bus.nextDepartureWaitTime( possibleDeparture );
            if ( busWaitTime < waitTime ) {
                waitTime = busWaitTime;
                waitBusId = bus.loopTime;
            }
        }
        long answer = waitBusId * waitTime;
        System.out.println( "  Bus wait * bus id: " + answer );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 13 - PART 2..." );
        //List<String> lines = Arrays.asList( "939", "7,13,x,x,59" );
        //List<String> lines = Arrays.asList( "939", "7,13,x,x,59,x,31,19" );
        //List<String> lines = Arrays.asList( "939", "17,x,13,19" );
        //List<String> lines = Arrays.asList( "939", "67,7,59,61" );
        //List<String> lines = Arrays.asList( "939", "67,x,7,59,61" );
        //List<String> lines = Arrays.asList( "939", "67,7,x,59,61" );
        //List<String> lines = Arrays.asList( "939", "1789,37,47,1889" );
        List<String> lines = new InputReader().readStringInput( "day13-input.txt" );

        List<Bus> buses = new ArrayList<>();
        int offset = 0;
        for ( String busEntry : lines.get(1).split( "," ) ) {
            if ( ! busEntry.equals( "x" ) ) {
                Bus bus = new Bus();
                bus.loopTime = Integer.parseInt( busEntry );
                bus.offset = offset;
                buses.add( bus );
            }
            offset++;
        }

        int solveForCnt = 1;
        int totalBusCnt = buses.size();
        long solution = 0;
        long searchItr = buses.get(0).loopTime;
        while ( solveForCnt < totalBusCnt ) {
            solveForCnt++;
            boolean rulesMet = false;
            while ( ! rulesMet ) {
                solution += searchItr;

                rulesMet = true;
                for ( int i = 0; i < solveForCnt; i++ ) {
                    Bus bus = buses.get( i );
                    if ( ! bus.isDepatureTime( solution + bus.offset ) ) {
                        rulesMet = false;
                        break;
                    }
                }
            }
            searchItr *= buses.get( solveForCnt - 1 ).loopTime;
        }

        System.out.println( "  Earliest time: " + solution );
    }

}

class Bus {
    int loopTime;
    int offset;
    long lastCalcTime;
    boolean isDepatureTime( long time ) {
        return time % loopTime == 0;
    }
    long nextDepartureTime( long afterTime ) {
        lastCalcTime = (((long) ( Math.ceil( afterTime / (double) loopTime ) ) ) * loopTime );
        return lastCalcTime;
    }
    long nextDepartureWaitTime( long afterTime ) {
        return nextDepartureTime( afterTime ) - afterTime;
    }
}
