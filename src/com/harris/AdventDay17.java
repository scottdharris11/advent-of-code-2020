package com.harris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventDay17 {

    public void executePart1() throws CloneNotSupportedException {
        System.out.println("ADVENT DAY 17 - PART 1...");
        /*List<String> lines = java.util.Arrays.asList(
            ".#.",
            "..#",
            "###"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day17-input.txt");

        Matrix matrix = initializeMatrix( lines );
        matrix.suppressPrint = true;
        matrix.print( "Matrix before cycles" );

        int cycleCnt = 6;
        int activeCubes = processCycles( matrix, cycleCnt, false );
        System.out.println( "  Active Cubes: " + activeCubes );
    }

    public void executePart2() throws CloneNotSupportedException {
        System.out.println("ADVENT DAY 17 - PART 2...");
        /*List<String> lines = java.util.Arrays.asList(
            ".#.",
            "..#",
            "###"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day17-input.txt");

        Matrix matrix = initializeMatrix( lines );
        matrix.suppressPrint = true;
        matrix.print( "Matrix before cycles" );

        int cycleCnt = 6;
        int activeCubes = processCycles( matrix, cycleCnt, true );
        System.out.println( "  Active Cubes: " + activeCubes );
    }

    private int processCycles( Matrix matrix, int cycleCnt, boolean fourDim )
    throws CloneNotSupportedException {
        int activeCubes = 0;
        for ( int i = 0; i < cycleCnt; i++ ) {
            Matrix updMatrix = matrix.clone();
            activeCubes = 0;

            int waveStart = matrix.minWaveW - 1;
            int waveEnd = matrix.maxWaveW + 1;
            if ( ! fourDim ) {
                waveStart = 0;
                waveEnd = 0;
            }
            int dimensionStart = matrix.minDimensionZ - 1;
            int dimensionEnd = matrix.maxDimensionZ + 1;
            int rowStart = matrix.minRowX - 1;
            int rowEnd = matrix.maxRowX + 1;
            int colStart = matrix.minColY - 1;
            int colEnd = matrix.maxColY + 1;

            for ( int w = waveStart; w <= waveEnd; w++ ) {
                for (int z = dimensionStart; z <= dimensionEnd; z++) {
                    for (int x = rowStart; x <= rowEnd; x++) {
                        for (int y = colStart; y <= colEnd; y++) {
                            Cube cube = matrix.findCube(w, z, x, y);
                            List<Integer[]> neighbors = findCubeNeighbors(w, z, x, y, fourDim);
                            int activeCnt = 0;
                            for (Integer[] neighbor : neighbors) {
                                Cube c = matrix.findCube(neighbor[0], neighbor[1], neighbor[2], neighbor[3]);
                                if (c.active) {
                                    activeCnt++;
                                }
                            }

                            if (cube.active) {
                                if (activeCnt < 2 || activeCnt > 3) {
                                    updMatrix.findCube(w, z, x, y).active = false;
                                } else {
                                    activeCubes++;
                                }
                            } else {
                                if (activeCnt == 3) {
                                    updMatrix.findCube(w, z, x, y).active = true;
                                    activeCubes++;
                                }
                            }
                        }
                    }
                }
            }

            matrix = updMatrix;
            matrix.print( "Matrix at cycle " + (i+1) );
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

    private Matrix initializeMatrix( List<String> lines ) {
        Matrix matrix = new Matrix();
        int w = 0;
        int z = 0;
        int x = 0;
        for ( String row : lines ) {
            char[] cols = row.toCharArray();
            for ( int y = 0; y < cols.length; y++ ) {
                matrix.findCube( w, z, x, y ).initializeActive( cols[y] );
            }
            x++;
        }

        return matrix;
    }

}

class Matrix implements Cloneable {
    boolean suppressPrint = false;
    int minWaveW = Integer.MAX_VALUE;
    int maxWaveW = Integer.MIN_VALUE;
    int minDimensionZ = Integer.MAX_VALUE;
    int maxDimensionZ = Integer.MIN_VALUE;
    int minRowX = Integer.MAX_VALUE;
    int maxRowX = Integer.MIN_VALUE;
    int minColY = Integer.MAX_VALUE;
    int maxColY = Integer.MIN_VALUE;
    Map<Integer,CubeWave> cubeWaves = new HashMap<>();

    Cube findCube( int w, int z, int x, int y ) {
        if ( w < minWaveW ) {
            minWaveW = z;
        }
        if ( w > maxWaveW ) {
            maxWaveW = z;
        }

        if ( z < minDimensionZ ) {
            minDimensionZ = z;
        }
        if ( z > maxDimensionZ ) {
            maxDimensionZ = z;
        }

        if ( x < minRowX ) {
            minRowX = x;
        }
        if ( x > maxRowX ) {
            maxRowX = x;
        }

        if ( y < minColY ) {
            minColY = y;
        }
        if ( y > maxColY ) {
            maxColY = y;
        }

        return findWave( w ).findCube( z, x, y );
    }

    void print( String preamble ) {
        if ( ! suppressPrint ) {
            System.out.println( preamble );
            for ( int w = minWaveW; w <= maxWaveW; w++ ) {
                for ( int z = minDimensionZ; z <= maxDimensionZ; z++ ) {
                    System.out.println("Wave " + w + ", Dimension " + z );
                    for ( int x = minRowX; x <= maxRowX; x++ ) {
                        for ( int y = minColY; y <= maxColY; y++ ) {
                            System.out.print( findCube( w, z, x, y ).activeChar() );
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        }
    }

    protected Matrix clone() throws CloneNotSupportedException {
        super.clone();
        Matrix m = new Matrix();
        m.suppressPrint = suppressPrint;
        m.minWaveW = minWaveW;
        m.maxWaveW = maxWaveW;
        m.minDimensionZ = minDimensionZ;
        m.maxDimensionZ = maxDimensionZ;
        m.minRowX = minRowX;
        m.maxRowX = maxRowX;
        m.minColY = minColY;
        m.maxColY = maxColY;
        for ( Map.Entry<Integer,CubeWave> cwEntry : cubeWaves.entrySet() ) {
            m.cubeWaves.put( cwEntry.getKey(), cwEntry.getValue().clone() );
        }
        return m;
    }

    private CubeWave findWave( int w ) {
        CubeWave cubeWave = cubeWaves.get( w );
        if ( cubeWave == null ) {
            cubeWave = new CubeWave();
            cubeWave.w = w;
            cubeWaves.put( w, cubeWave );
        }
        return cubeWave;
    }

}

class CubeWave implements Cloneable {
    int w;
    int minDimensionZ = Integer.MAX_VALUE;
    int maxDimensionZ = Integer.MIN_VALUE;
    Map<Integer,CubeDimension> cubeDimensions = new HashMap<>();

    Cube findCube( int z, int x, int y ) {
        return findDimension( z ).findCube( x, y );
    }

    protected CubeWave clone() throws CloneNotSupportedException {
        super.clone();
        CubeWave cw = new CubeWave();
        cw.w = w;
        cw.minDimensionZ = minDimensionZ;
        cw.maxDimensionZ = maxDimensionZ;
        for ( Map.Entry<Integer,CubeDimension> cubeDimensionEntryEntry : cubeDimensions.entrySet() ) {
            cw.cubeDimensions.put( cubeDimensionEntryEntry.getKey(), cubeDimensionEntryEntry.getValue().clone() );
        }
        return cw;
    }

    private CubeDimension findDimension( int z ) {
        if ( z < minDimensionZ ) {
            minDimensionZ = z;
        }
        if ( z > maxDimensionZ ) {
            maxDimensionZ = z;
        }

        CubeDimension cubeDimension = cubeDimensions.get( z );
        if ( cubeDimension == null ) {
            cubeDimension = new CubeDimension();
            cubeDimension.z = z;
            cubeDimensions.put( z, cubeDimension );
        }
        return cubeDimension;
    }

}

class CubeDimension implements Cloneable {
    int z;
    int minRowX = Integer.MAX_VALUE;
    int maxRowX = Integer.MIN_VALUE;
    Map<Integer,CubeRow> cubeRows = new HashMap<>();

    Cube findCube( int x, int y ) {
        return findRow( x ).findCube( y );
    }

    protected CubeDimension clone() throws CloneNotSupportedException {
        super.clone();
        CubeDimension cd = new CubeDimension();
        cd.z = z;
        cd.minRowX = minRowX;
        cd.maxRowX = maxRowX;
        for ( Map.Entry<Integer,CubeRow> cubeRowEntry : cubeRows.entrySet() ) {
            cd.cubeRows.put( cubeRowEntry.getKey(), cubeRowEntry.getValue().clone() );
        }
        return cd;
    }

    private CubeRow findRow( int x ) {
        if ( x < minRowX ) {
            minRowX = x;
        }
        if ( x > maxRowX ) {
            maxRowX = x;
        }

        CubeRow cubeRow = cubeRows.get( x );
        if ( cubeRow == null ) {
            cubeRow = new CubeRow();
            cubeRow.x = x;
            cubeRows.put( x, cubeRow );
        }
        return cubeRow;
    }

}

class CubeRow implements Cloneable {
    int x;
    int minColY = Integer.MAX_VALUE;
    int maxColY = Integer.MIN_VALUE;
    Map<Integer,Cube> cubes = new HashMap<>();

    Cube findCube( int y ) {
        if ( y < minColY ) {
            minColY = y;
        }
        if ( y > maxColY ) {
            maxColY = y;
        }

        Cube cube = cubes.get( y );
        if ( cube == null ) {
            cube = new Cube();
            cube.y = y;
            cubes.put( y, cube );
        }
        return cube;
    }

    protected CubeRow clone() throws CloneNotSupportedException {
        super.clone();
        CubeRow cr = new CubeRow();
        cr.x = x;
        cr.minColY = minColY;
        cr.maxColY = maxColY;
        for ( Map.Entry<Integer,Cube> cubeEntry : cubes.entrySet() ) {
            cr.cubes.put( cubeEntry.getKey(), cubeEntry.getValue().clone() );
        }
        return cr;
    }
}

class Cube implements Cloneable {
    private static final char ACTIVE = '#';
    private static final char INACTIVE = '.';
    int y;
    boolean active;
    void initializeActive( char c ) {
        active = c == ACTIVE;
    }
    char activeChar() { return active ? ACTIVE : INACTIVE; }
    protected Cube clone() throws CloneNotSupportedException {
        super.clone();
        Cube c = new Cube();
        c.y = y;
        c.active = active;
        return c;
    }
}
