package com.harris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdventDay5 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 5 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "day5-input.txt" );
        int highSeatId = 0;
        for ( String line : lines ) {
            int seatId = convertToSeatId( line );
            if ( seatId > highSeatId ) {
                highSeatId = seatId;
            }
        }
        System.out.println( "  High Seat Id: "+ highSeatId );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 5 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "day5-input.txt" );
        List<Integer> takenIds = new ArrayList<>();
        for ( String line : lines ) {
            takenIds.add( convertToSeatId( line ) );
        }

        Collections.sort( takenIds );

        int mySeatId = -1;
        int prevSeatId = -1;
        for ( int seatId : takenIds ) {
            if ( prevSeatId > -1 && prevSeatId + 1 != seatId ) {
                mySeatId = prevSeatId + 1;
                break;
            }
            prevSeatId = seatId;
        }

        System.out.println( "  Seat Id: " + mySeatId );
    }

    private int convertToSeatId( String value ) {
        int row = convertToNumber( value.substring( 0, 7 ), 'F', 'B', 127 );
        int seat = convertToNumber( value.substring( 7 ), 'L', 'R', 7 );
        return ( row * 8 ) + seat;
    }

    private int convertToNumber( String value, char front, char back, int max ) {
        int lowVal = 0;
        int highVal = max;
        for ( char c : value.toCharArray() ) {
            int offset = (int) Math.ceil( ( highVal - lowVal ) / 2.0 );
            if ( c == front ) {
                highVal -= offset;
            } else if ( c == back ) {
                lowVal += offset;
            }
        }
        return lowVal;
    }

}
