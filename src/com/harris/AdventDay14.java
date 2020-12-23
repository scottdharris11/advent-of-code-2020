package com.harris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventDay14 {

    public void executePart1() {
        System.out.println("ADVENT DAY 14 - PART 1...");
        //List<String> lines = java.util.Arrays.asList(
        //        "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X",
        //        "mem[8] = 11",
        //        "mem[7] = 101",
        //        "mem[8] = 0" );
        List<String> lines = new InputReader().readStringInput("data-files/day14-input.txt" );

        Map<Long,Long> memory = new HashMap<>();
        String currMask = "";
        for ( String line : lines ) {
            if ( line.startsWith( "mask" ) ) {
                currMask = parseMask( line );
            } else if ( line.startsWith( "mem" ) ) {
                BitCommand command = parseMemCommand( line );
                memory.put( command.memLoc, applyMaskToValue( command.value, currMask ) );
            }
        }

        long sumMemory = 0;
        for ( long value : memory.values() ) {
            sumMemory += value;
        }
        System.out.println( "  Sum of memory values: " + sumMemory );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 14 - PART 2...");
        //List<String> lines = java.util.Arrays.asList(
        //        "mask = 000000000000000000000000000000X1001X",
        //        "mem[42] = 100",
        //        "mask = 00000000000000000000000000000000X0XX",
        //        "mem[26] = 1" );
        List<String> lines = new InputReader().readStringInput("data-files/day14-input.txt" );

        Map<Long,Long> memory = new HashMap<>();
        String currMask = "";
        for ( String line : lines ) {
            if ( line.startsWith( "mask" ) ) {
                currMask = parseMask( line );
            } else if ( line.startsWith( "mem" ) ) {
                BitCommand command = parseMemCommand( line );
                List<Long> addresses = buildMemAddresses( command.memLoc, currMask );
                for ( long address : addresses ) {
                    memory.put( address, command.value );
                }
            }
        }

        long sumMemory = 0;
        for ( long value : memory.values() ) {
            sumMemory += value;
        }
        System.out.println( "  Sum of memory values: " + sumMemory );
    }

    private List<Long> buildMemAddresses( long memLoc, String mask ) {
        List<Integer> floatingLocations = new ArrayList<>();
        long baseValue = memLoc;
        int bitPos = 35;
        char[] maskValues = mask.toCharArray();
        for ( int i = 0; i < maskValues.length; i++, bitPos-- ) {
            if ( maskValues[i] == 'X' ) {
                floatingLocations.add( bitPos );
            } else if ( maskValues[i] == '1' ) {
                baseValue = modifyBit( baseValue, bitPos, 1 );
            }
        }

        int floatingCnt = floatingLocations.size();
        List<String> permutations = new ArrayList<>();
        int[] arr = new int[floatingCnt];
        generateAllBinaryStrings( floatingCnt, arr, 0, permutations );

        List<Long> addresses = new ArrayList<>();
        for ( String permutation : permutations ) {
            char[] bitValues = permutation.toCharArray();
            long addressVal = baseValue;
            for ( int i = 0; i < bitValues.length; i++ ) {
                addressVal = modifyBit( addressVal, floatingLocations.get( i ), bitValues[i] == '0' ? 0 : 1 );
            }
            addresses.add( addressVal );
        }
        return addresses;
    }

    private void generateAllBinaryStrings( int n, int[] arr, int i, List<String> permutations ) {
        if (i == n) {
            addComboToList( arr, n, permutations );
            return;
        }

        // First assign "0" at ith position
        // and try for all other permutations
        // for remaining positions
        arr[i] = 0;
        generateAllBinaryStrings( n, arr, i + 1, permutations );

        // And then assign "1" at ith position
        // and try for all other permutations
        // for remaining positions
        arr[i] = 1;
        generateAllBinaryStrings( n, arr, i + 1, permutations );
    }

    private void addComboToList(int[] arr, int n, List<String> permutations ) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(arr[i]);
        }
        permutations.add( sb.toString() );
    }

    private long applyMaskToValue( long value, String mask ) {
        char[] maskValues = mask.toCharArray();
        int bitPos = 35;
        long adjustedVal = value;
        boolean printData = value == 119;
        if ( printData ) {
            System.out.println( "Mask  : " + mask );
            System.out.println( "Before: " + binaryString( adjustedVal ) );
        }
        for ( int i = 0; i < maskValues.length; i++, bitPos-- ) {
            if ( maskValues[i] != 'X' ) {
                adjustedVal = modifyBit( adjustedVal, bitPos, maskValues[i] == '1' ? 1 : 0 );
            }
        }
        if ( printData ) {
            System.out.println( "After : " + binaryString( adjustedVal ) );
        }
        return adjustedVal;
    }

    private String binaryString( long value ) {
        String strValue = Long.toBinaryString( value );
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < 36 - strValue.length(); i++ ) {
            sb.append( '0' );
        }
        sb.append( strValue );
        return sb.toString();
    }

    private long modifyBit( long value, long pos, long b ) {
        long mask = 1L << pos;
        return (value & ~mask) | ((b << pos) & mask);
    }

    private String parseMask( String line ) {
        return line.split( " " )[2];
    }

    private BitCommand parseMemCommand( String line ) {
        String[] entries = line.split( " " );
        int beginMemLoc = entries[0].indexOf( '[' );
        int endMemLoc = entries[0].indexOf( ']' );
        String memLoc = entries[0].substring( beginMemLoc + 1, endMemLoc );

        BitCommand command = new BitCommand();
        command.memLoc = Long.parseUnsignedLong( memLoc );
        command.value = Long.parseUnsignedLong( entries[2] );
        return command;
    }

}

class BitCommand {
    long memLoc;
    long value;
}