package com.harris;

import java.util.List;

public class AdventDay11 {

    private static final char EMPTY = 'L';
    private static final char TAKEN = '#';

    public void executePart1() {
        System.out.println( "ADVENT DAY 11 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "data-files/day11-input.txt" );
        char[][] seatingChart = buildSeatingChart( lines );

        int seatsOccupied = 0;
        boolean applySeatingRules = true;
        int roundsApplied = 0;
        while ( applySeatingRules ) {
            char[][] adjustedChart = new char[seatingChart.length][seatingChart[0].length];
            applySeatingRules = false;
            seatsOccupied = 0;
            for ( int i = 0; i < seatingChart.length; i++ ) {
                for ( int j = 0; j < seatingChart[i].length; j++ ) {
                    if ( seatingChart[i][j] == EMPTY && countAdjacentOccupied( seatingChart, i, j ) == 0 ) {
                        adjustedChart[i][j] = TAKEN;
                        applySeatingRules = true;
                    } else if ( seatingChart[i][j] == TAKEN && countAdjacentOccupied( seatingChart, i, j ) >= 4 ) {
                        adjustedChart[i][j] = EMPTY;
                        applySeatingRules = true;
                    } else {
                        adjustedChart[i][j] = seatingChart[i][j];
                    }

                    if ( adjustedChart[i][j] == TAKEN ) {
                        seatsOccupied++;
                    }
                }
            }
            seatingChart = adjustedChart;
            roundsApplied++;
        }

        System.out.println( "  Rounds applied: " + roundsApplied + ", Seats occupied: " + seatsOccupied );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 11 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "data-files/day11-input.txt" );
        char[][] seatingChart = buildSeatingChart( lines );

        int seatsOccupied = 0;
        boolean applySeatingRules = true;
        int roundsApplied = 0;
        while ( applySeatingRules ) {
            char[][] adjustedChart = new char[seatingChart.length][seatingChart[0].length];
            applySeatingRules = false;
            seatsOccupied = 0;
            for ( int i = 0; i < seatingChart.length; i++ ) {
                for ( int j = 0; j < seatingChart[i].length; j++ ) {
                    if ( seatingChart[i][j] == EMPTY && countAdjacentVisibleOccupied( seatingChart, i, j ) == 0 ) {
                        adjustedChart[i][j] = TAKEN;
                        applySeatingRules = true;
                    } else if ( seatingChart[i][j] == TAKEN && countAdjacentVisibleOccupied( seatingChart, i, j ) >= 5 ) {
                        adjustedChart[i][j] = EMPTY;
                        applySeatingRules = true;
                    } else {
                        adjustedChart[i][j] = seatingChart[i][j];
                    }

                    if ( adjustedChart[i][j] == TAKEN ) {
                        seatsOccupied++;
                    }
                }
            }
            seatingChart = adjustedChart;
            roundsApplied++;
        }

        System.out.println( "  Rounds applied: " + roundsApplied + ", Seats occupied: " + seatsOccupied );
    }

    private char[][] buildSeatingChart( List<String> lines ) {
        char[][] seatingChart = new char[lines.size()][lines.get(0).length()];
        int i = 0;
        for ( String line : lines ) {
            seatingChart[i] = line.toCharArray();
            i++;
        }
        return seatingChart;
    }

    private int countAdjacentOccupied( char[][] seatingChart, int row, int col ) {
        int occupiedCnt = 0;
        for ( int i = -1; i <= 1; i++ ) {
            for ( int j = -1; j <= 1; j++ ) {
                if ( i == 0 && j == 0 ) {
                    continue;
                }
                int checkRow = row + i;
                int checkCol = col + j;
                if ( spotExists( seatingChart, checkRow, checkCol ) ) {
                    if ( seatingChart[checkRow][checkCol] == TAKEN ) {
                        occupiedCnt++;
                    }
                }
            }
        }
        return occupiedCnt;
    }

    private int countAdjacentVisibleOccupied( char[][] seatingChart, int row, int col ) {
        int[][] adjacentPaths = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        int occupiedCnt = 0;
        for ( int[] adjacentPath : adjacentPaths ) {
            int checkRow = row;
            int checkCol = col;
            while ( true ) {
                checkRow += adjacentPath[0];
                checkCol += adjacentPath[1];
                if ( spotExists( seatingChart, checkRow, checkCol ) ) {
                    if ( seatingChart[checkRow][checkCol] == TAKEN ) {
                        occupiedCnt++;
                        break;
                    } else if ( seatingChart[checkRow][checkCol] == EMPTY ) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return occupiedCnt;
    }

    private boolean spotExists( char[][] seatingChart, int row, int col ) {
        return ( row >= 0 && row < seatingChart.length && col >= 0 && col < seatingChart[0].length );
    }

}
