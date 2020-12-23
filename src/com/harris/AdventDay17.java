package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay17 {

    public void executePart1() {
        System.out.println("ADVENT DAY 17 - PART 1a...");
        /*List<String> lines = java.util.Arrays.asList(
            ".#.",
            "..#",
            "###"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day17-input.txt");

        HyperMatrix matrix = initializeHyperMatrix( lines );
        matrix.suppressPrint = true;
        matrix.print( "Matrix before cycles" );

        Instant start = Instant.now();
        int cycleCnt = 6;
        int activeCubes = processCycles( matrix, cycleCnt, false );
        Instant end = Instant.now();
        System.out.println( "  [" + Duration.between( start, end ) + "] Active Cubes: " + activeCubes );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 17 - PART 2a...");
        /*List<String> lines = java.util.Arrays.asList(
            ".#.",
            "..#",
            "###"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day17-input.txt");

        HyperMatrix matrix = initializeHyperMatrix( lines );
        matrix.suppressPrint = true;
        matrix.print( "Matrix before cycles" );

        Instant start = Instant.now();
        int cycleCnt = 6;
        int activeCubes = processCycles( matrix, cycleCnt, true );
        Instant end = Instant.now();
        System.out.println( "  [" + Duration.between( start, end ) + "] Active Cubes: " + activeCubes );
    }

    private HyperMatrix initializeHyperMatrix( List<String> lines ) {
        HyperMatrix matrix = new HyperMatrix();
        int w = 0;
        int z = 0;
        int x = 0;
        for ( String row : lines ) {
            char[] cols = row.toCharArray();
            for ( int y = 0; y < cols.length; y++ ) {
                matrix.registerCube( w, z, x, y, cols[y] == '#' );
            }
            x++;
        }
        return matrix;
    }

    private int processCycles( HyperMatrix matrix, int cycleCnt, boolean fourDim ) {
        int activeCubes = 0;
        for ( int i = 0; i < cycleCnt; i++ ) {
            matrix.startWorkingSession();
            activeCubes = 0;

            int waveStart = matrix.minW - 1;
            int waveEnd = matrix.maxW + 1;
            if ( ! fourDim ) {
                waveStart = 0;
                waveEnd = 0;
            }
            int dimensionStart = matrix.minZ - 1;
            int dimensionEnd = matrix.maxZ + 1;
            int rowStart = matrix.minX - 1;
            int rowEnd = matrix.maxX + 1;
            int colStart = matrix.minY - 1;
            int colEnd = matrix.maxY + 1;

            for ( int w = waveStart; w <= waveEnd; w++ ) {
                for (int z = dimensionStart; z <= dimensionEnd; z++) {
                    for (int x = rowStart; x <= rowEnd; x++) {
                        for (int y = colStart; y <= colEnd; y++) {
                            List<Integer[]> neighbors = findCubeNeighbors(w, z, x, y, fourDim);
                            int activeCnt = 0;
                            for ( Integer[] n : neighbors ) {
                                if ( matrix.isCubeActive( n[0], n[1], n[2], n[3] ) ) {
                                    activeCnt++;
                                }
                            }

                            boolean active = matrix.isCubeActive( w, z, x, y );
                            if ( active ) {
                                if (activeCnt < 2 || activeCnt > 3) {
                                    active = false;
                                } else {
                                    activeCubes++;
                                }
                            } else {
                                if ( activeCnt == 3 ) {
                                    active = true;
                                    activeCubes++;
                                }
                            }
                            if ( active ) {
                                matrix.registerCube( w, z, x, y, true );
                            }
                        }
                    }
                }
            }

            matrix.commitWorkingSession();
            matrix.print( "Matrix after cycle " + (i+1) + ", active: " + activeCubes );
        }
        return activeCubes;
    }

    private List<Integer[]> findCubeNeighbors( int w, int z, int x, int y, boolean fourDim ) {
        List<Integer[]> neighbors = new ArrayList<>();

        int wStart = w - 1;
        int wEnd = w + 1;
        if ( ! fourDim ) {
            wStart = 0;
            wEnd = 0;
        }

        for ( int a = wStart; a <= wEnd; a++ ) {
            for ( int b = z - 1; b <= z + 1; b++ ) {
                for ( int c = x - 1; c <= x + 1; c++ ) {
                    for ( int d = y - 1; d <= y + 1; d++) {
                        if ( a != w || b != z || c != x || d != y ) {
                            neighbors.add(new Integer[]{a, b, c, d});
                        }
                    }
                }
            }
        }

        return neighbors;
    }

}

class HyperMatrix {
    boolean suppressPrint = false;
    int minW = Integer.MAX_VALUE;
    int maxW = Integer.MIN_VALUE;
    int minZ = Integer.MAX_VALUE;
    int maxZ = Integer.MIN_VALUE;
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    Set<Object> activeCubes = new HashSet<>();
    Set<Object> workingActiveCubes = activeCubes;

    void registerCube( int w, int z, int x, int y, boolean active ) {
        if ( w < minW ) { minW = w; }
        if ( w > maxW ) { maxW = w; }
        if ( z < minZ ) { minZ = z; }
        if ( z > maxZ ) { maxZ = z; }
        if ( x < minX ) { minX = x; }
        if ( x > maxX ) { maxX = x; }
        if ( y < minY ) { minY = y; }
        if ( y > maxY ) { maxY = y; }

        if ( active ) {
            workingActiveCubes.add( buildCubeKey( w, z, x, y ) );
        }
    }

    boolean isCubeActive( int w, int z, int x, int y ) {
        return activeCubes.contains( buildCubeKey( w, z, x, y ) );
    }

    void startWorkingSession() {
        workingActiveCubes = new HashSet<>();
    }

    void commitWorkingSession() {
        activeCubes = workingActiveCubes;
    }

    void print( String preamble ) {
        if ( ! suppressPrint ) {
            System.out.println( preamble );
            for ( int w = minW; w <= maxW; w++ ) {
                for ( int z = minZ; z <= maxZ; z++ ) {
                    System.out.println("Wave " + w + ", Dimension " + z );
                    for ( int x = minX; x <= maxX; x++ ) {
                        for ( int y = minY; y <= maxY; y++ ) {
                            Object cubeKey = buildCubeKey( w, z, x, y );
                            System.out.print( activeCubes.contains( cubeKey ) ? '#' : '.' );
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        }
    }

    private Object buildCubeKey( int w, int z, int x, int y ) {
        return w + ":" + z + ":" + x + ":" + y;
    }
}