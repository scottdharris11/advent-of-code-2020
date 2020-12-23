package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AdventDay18 {

    public void executePart1() {
        System.out.println("ADVENT DAY 18 - PART 1...");
        //List<String> lines = java.util.Collections.singletonList( "1 + 2 * 3 + 4 * 5 + 6" );
        //List<String> lines = java.util.Collections.singletonList( "1 + (2 * 3) + (4 * (5 + 6))" );
        //List<String> lines = java.util.Collections.singletonList( "2 * 3 + (4 * 5)" );
        //List<String> lines = java.util.Collections.singletonList( "5 + (8 * 3 + 9 + 3 * 4 * 3)" );
        //List<String> lines = java.util.Collections.singletonList( "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))" );
        //List<String> lines = java.util.Collections.singletonList( "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" );
        //List<String> lines = java.util.Collections.singletonList( "4 * 9 * ((9 + 2) + 4 + 2 * 5) * ((6 * 9 * 5 + 8) * (7 + 7)) * (8 * 7 + 9 + 7 * (2 + 9 + 9 + 4 + 8) + 9) * 6" );

        List<String> lines = new InputReader().readStringInput("data-files/day18-input.txt" );

        Instant start = Instant.now();
        long answer = 0;
        for ( String line : lines ) {
            long lineAnswer = calculateValue( line, new CalculationState(), false );
            //System.out.println( "  " + line + " = " + lineAnswer );
            answer += lineAnswer;
        }
        Instant end = Instant.now();
        System.out.println( "  [" + Duration.between( start, end ) + "] Answer: " + answer );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 18 - PART 2...");
        //List<String> lines = java.util.Collections.singletonList( "1 + 2 * 3 + 4 * 5 + 6" );
        //List<String> lines = java.util.Collections.singletonList( "1 + (2 * 3) + (4 * (5 + 6))" );
        //List<String> lines = java.util.Collections.singletonList( "2 * 3 + (4 * 5)" );
        //List<String> lines = java.util.Collections.singletonList( "5 + (8 * 3 + 9 + 3 * 4 * 3)" );
        //List<String> lines = java.util.Collections.singletonList( "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))" );
        //List<String> lines = java.util.Collections.singletonList( "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2" );
        //List<String> lines = java.util.Collections.singletonList( "4 * 9 * ((9 + 2) + 4 + 2 * 5) * ((6 * 9 * 5 + 8) * (7 + 7)) * (8 * 7 + 9 + 7 * (2 + 9 + 9 + 4 + 8) + 9) * 6" );

        List<String> lines = new InputReader().readStringInput("data-files/day18-input.txt" );

        Instant start = Instant.now();
        long answer = 0;
        for ( String line : lines ) {
            long lineAnswer = calculateValue( line, new CalculationState(), true );
            //System.out.println( "  " + line + " = " + lineAnswer );
            answer += lineAnswer;
        }
        Instant end = Instant.now();
        System.out.println( "  [" + Duration.between( start, end ) + "] Answer: " + answer );
    }

    private long calculateValue( String line, CalculationState calcState, boolean applyAddsFirst ) {
        char[] chars = line.toCharArray();
        List<CalculationOperation> operations = new ArrayList<>();
        Character operation = null;
        for ( int i = calcState.lineIdx; i < chars.length; i++ ) {
            if ( chars[i] == '+' || chars[i] == '*' ) {
                operation = chars[i];
            } else if ( Character.isDigit( chars[i] ) ) {
                StringBuilder sb = new StringBuilder();
                sb.append( chars[i] );
                int numIdx = i + 1;
                while ( numIdx < chars.length ) {
                    if ( Character.isDigit( chars[numIdx] ) ) {
                        sb.append( chars[numIdx] );
                        numIdx++;
                    } else {
                        break;
                    }
                }
                i = numIdx - 1;
                long activeDigit = Long.parseLong( sb.toString() );

                CalculationOperation op = new CalculationOperation();
                op.value = activeDigit;
                op.operation = operation == null ? '+' : operation;
                operations.add( op );
                operation = null;
            } else if ( chars[i] == '(' ) {
                calcState.lineIdx = i + 1;
                long activeDigit = calculateValue( line, calcState, applyAddsFirst );
                i = calcState.lineIdx;

                CalculationOperation op = new CalculationOperation();
                op.value = activeDigit;
                op.operation = operation == null ? '+' : operation;
                operations.add( op );
                operation = null;
            } else if ( chars[i] == ')' ) {
                calcState.lineIdx = i;
                break;
            }
        }
        return buildResult( operations, applyAddsFirst );
    }

    private long buildResult( List<CalculationOperation> operations, boolean applyAddsFirst ) {
        if ( applyAddsFirst ) {
            operations = consolidateAdds( operations );
        }
        long result = 0;
        for ( CalculationOperation op : operations ) {
            if ( op.operation == '+' ) {
                result += op.value;
            } else {
                result *= op.value;
            }
        }
        return result;
    }

    private List<CalculationOperation> consolidateAdds( List<CalculationOperation> operations ) {
        List<CalculationOperation> adjOps = new ArrayList<>();
        CalculationOperation holdOp = null;
        for ( CalculationOperation op : operations ) {
            if ( holdOp == null ) {
                holdOp = op;
            } else {
                if ( op.operation == '*' ) {
                    adjOps.add( holdOp );
                    holdOp = op;
                } else {
                    holdOp.value += op.value;
                }
            }
        }
        adjOps.add( holdOp );

        return adjOps;
    }

}

class CalculationState {
    int lineIdx = 0;
}

class CalculationOperation {
    char operation;
    long value;
}