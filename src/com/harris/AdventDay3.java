package com.harris;

import java.util.List;

public class AdventDay3 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 3 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "data-files/day3-input.txt" );
        int treeCnt = countTrees( lines, 3, 1 );
        System.out.println("  Trees encountered: " + treeCnt );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 3 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "data-files/day3-input.txt" );
        long treeCnt1 = countTrees( lines, 1, 1 );
        long treeCnt2 = countTrees( lines, 3, 1 );
        long treeCnt3 = countTrees( lines, 5, 1 );
        long treeCnt4 = countTrees( lines, 7, 1 );
        long treeCnt5 = countTrees( lines, 1, 2 );
        long result = treeCnt1 * treeCnt2 * treeCnt3 * treeCnt4 * treeCnt5;
        System.out.println("  Trees encountered: " + result );
    }

    private int countTrees( List<String> lines, int slopeX, int slopeY ) {
        int lineCnt = lines.size();
        int lineLen = lines.get(0).length();
        int indexX = 0, indexY = 0;
        int treeCnt = 0;
        while ( indexY < lineCnt ) {
            int checkXIdx = indexX;
            while ( checkXIdx >= lineLen ) {
                checkXIdx -= lineLen;
            }

            String line = lines.get( indexY );
            if ( indexX > 0 && '#' == line.toCharArray()[checkXIdx] ) {
                treeCnt++;
            }
            indexX += slopeX;
            indexY += slopeY;
        }
        return treeCnt;
    }

}
