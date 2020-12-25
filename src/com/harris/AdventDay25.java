package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AdventDay25 {

    private static final long SUBJECT_NUMBER = 7L;
    private static final long DIVISION_VAL = 20201227L;

    public void executePart1() {
        System.out.println("ADVENT DAY 25 - PART 1...");
        //List<Long> publicKeys = java.util.Arrays.asList( 5764801L, 17807724L );
        List<Long> publicKeys = java.util.Arrays.asList( 10212254L, 12577395L );

        Instant start = Instant.now();

        long loopCnt = findLoopSize( publicKeys.get( 1 ) );
        long encryptKey = calcKey( publicKeys.get( 0 ), loopCnt );

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Encryption Key: " + encryptKey );
    }

    private long findLoopSize( long key ) {
        long wrkKey = 1;
        long loopCnt = 0;
        while ( wrkKey != key ) {
            loopCnt++;
            wrkKey = wrkKey * SUBJECT_NUMBER % DIVISION_VAL;
        }
        return loopCnt;
    }

    private long calcKey( long subjectNo, long loopSize ) {
        long value = 1;
        for ( int i = 0; i < loopSize; i++ ) {
            value *= subjectNo;
            value = value % DIVISION_VAL;
        }
        return value;
    }

}
