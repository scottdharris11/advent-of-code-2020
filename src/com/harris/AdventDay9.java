package com.harris;

import java.util.List;

public class AdventDay9 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 9 - PART 1..." );
        List<Long> numbers = new InputReader().readLongInput( "data-files/day9-input.txt" );
        long workValue = findFirstMisMatch( numbers );
        System.out.println( "  First non-matching value: " + workValue );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 9 - PART 2..." );
        List<Long> numbers = new InputReader().readLongInput( "data-files/day9-input.txt" );
        int numberCnt = numbers.size();
        long workValue = findFirstMisMatch( numbers );

        int scanBegin = 0;
        int scanEnd = 0;
        long smallest = Long.MAX_VALUE;
        long largest = Long.MIN_VALUE;
        done:
        while ( scanBegin < numberCnt ) {
            long wrkSum = 0;
            long tmpVal;
            while ( scanEnd < numberCnt ) {
                tmpVal = numbers.get( scanEnd );
                wrkSum += tmpVal;
                if ( tmpVal < smallest ) {
                    smallest = tmpVal;
                }
                if ( tmpVal > largest ) {
                    largest = tmpVal;
                }

                if ( wrkSum == workValue ) {
                    break done;
                } else if ( wrkSum > workValue ) {
                    break;
                }
                scanEnd++;
            }

            scanBegin++;
            scanEnd = scanBegin;
            smallest = Long.MAX_VALUE;
            largest = Long.MIN_VALUE;
        }

        long weakness = smallest + largest;
        System.out.println( "  Weakness value: " + weakness );
    }

    private long findFirstMisMatch( List<Long> numbers ) {
        int rangeCnt = 25;
        int workingIdx = 25;
        int numberCnt = numbers.size();
        long workValue = 0;
        while ( workingIdx < numberCnt ) {
            workValue = numbers.get( workingIdx );
            if ( findSumMisMatch( numbers, workValue, workingIdx, rangeCnt ) ) {
                break;
            }
            workingIdx++;
        }
        return workValue;
    }

    private boolean findSumMisMatch( List<Long> numbers, long value, int currIdx, int range ) {
        int checkStartIdx = currIdx - range;
        int checkEndIdx = currIdx - 1;
        for ( int x = checkStartIdx; x <= checkEndIdx; x++ ) {
            for ( int y = checkStartIdx; y <= checkEndIdx; y++ ) {
                if ( x != y ) {
                    long sum = numbers.get( x ) + numbers.get( y );
                    if ( sum == value ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
