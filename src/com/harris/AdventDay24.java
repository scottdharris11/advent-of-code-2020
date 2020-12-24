package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay24 {

    private static final int[] E = { -10, 0 };
    private static final int[] W = { 10, 0 };
    private static final int[] NE = { -5, 10 };
    private static final int[] NW = { 5, 10 };
    private static final int[] SE = { -5, -10 };
    private static final int[] SW = { 5, -10 };

    public void executePart1() {
        System.out.println("ADVENT DAY 24 - PART 1...");
        /*List<String> lines = Arrays.asList(
                "sesenwnenenewseeswwswswwnenewsewsw",
                "neeenesenwnwwswnenewnwwsewnenwseswesw",
                "seswneswswsenwwnwse",
                "nwnwneseeswswnenewneswwnewseswneseene",
                "swweswneswnenwsewnwneneseenw",
                "eesenwseswswnenwswnwnwsewwnwsene",
                "sewnenenenesenwsewnenwwwse",
                "wenwwweseeeweswwwnwwe",
                "wsweesenenewnwwnwsenewsenwwsesesenwne",
                "neeswseenwwswnwswswnw",
                "nenwswwsewswnenenewsenwsenwnesesenew",
                "enewnwewneswsewnwswenweswnenwsenwsw",
                "sweneswneswneneenwnewenewwneswswnese",
                "swwesenesewenwneswnwwneseswwne",
                "enesenwswwswneneswsenwnewswseenwsese",
                "wnwnesenesenenwwnenwsewesewsesesew",
                "nenewswnwewswnenesenwnesewesw",
                "eneswnwswnwsenenwnwnwwseeswneewsenese",
                "neswnwewnwnwseenwseesewsenwsweewe",
                "wseweeenwnesenwwwswnew"
        );*/
        //List<String> lines = java.util.Collections.singletonList( "esew" );
        //List<String> lines = java.util.Collections.singletonList( "nwwswee" );
        List<String> lines = new InputReader().readStringInput("data-files/day24-input.txt" );

        Instant start = Instant.now();
        Set<String> blackTiles = new HashSet<>();
        List<List<String>> paths = parsePathDirections( lines );
        for ( List<String> path : paths ) {
            String tileId = followPathToTile( path );
            if ( blackTiles.contains( tileId ) ) {
                blackTiles.remove( tileId );
            } else {
                blackTiles.add( tileId );
            }
        }

        //System.out.println( "  Black Tiles: " + blackTiles );
        int answer = blackTiles.size();
        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer );
    }

    private String followPathToTile( List<String> directions ) {
        int[] loc = { 0, 0 };
        for ( String dir : directions ) {
            int[] move;
            switch ( dir ) {
                case "e":
                    move = E;
                    break;
                case "w":
                    move = W;
                    break;
                case "ne":
                    move = NE;
                    break;
                case "nw":
                    move = NW;
                    break;
                case "se":
                    move = SE;
                    break;
                case "sw":
                default:
                    move = SW;
                    break;
            }
            loc[0] += move[0];
            loc[1] += move[1];
        }
        return loc[0] + "-" + loc[1];
    }

    private List<List<String>> parsePathDirections( List<String> lines ) {
        List<List<String>> paths = new ArrayList<>();
        for ( String line : lines ) {
            List<String> directions = new ArrayList<>();
            int lineLen = line.length();
            int lineIdx = 0;
            while ( lineIdx < lineLen ) {
                char readChar = line.charAt( lineIdx );
                if ( readChar == 's' || readChar == 'n' ) {
                    directions.add( line.substring( lineIdx, lineIdx + 2 ) );
                    lineIdx += 2;
                } else {
                    directions.add( String.valueOf( readChar ) );
                    lineIdx++;
                }
            }
            paths.add( directions );
        }
        return paths;
    }

}
