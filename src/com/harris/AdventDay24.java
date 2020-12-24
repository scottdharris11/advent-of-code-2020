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

        List<List<String>> paths = parsePathDirections( lines );
        Set<HexTile> blackTiles = initializeFloor( paths );

        //System.out.println( "  Black Tiles: " + blackTiles );
        int answer = blackTiles.size();
        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 24 - PART 2...");
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
        List<List<String>> paths = parsePathDirections( lines );
        Set<HexTile> blackTiles = initializeFloor( paths );
        for ( int i = 0; i < 100; i++ ) {
            blackTiles = adjustTiles( blackTiles );
            //System.out.println( "  Day " + (i+1) + " Black Tiles: " + blackTiles.size() );
        }

        int answer = blackTiles.size();
        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer );
    }

    private Set<HexTile> initializeFloor( List<List<String>> paths ) {
        Set<HexTile> blackTiles = new HashSet<>();
        for ( List<String> path : paths ) {
            HexTile tile = followPathToTile( path, null );
            if ( blackTiles.contains( tile ) ) {
                blackTiles.remove( tile );
            } else {
                blackTiles.add( tile );
            }
        }
        return blackTiles;
    }

    private Set<HexTile> adjustTiles( Set<HexTile> blackTiles ) {
        Set<HexTile> afterBlackTiles = new HashSet<>();

        // Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white
        List<HexTile> allAdjacentTiles = new ArrayList<>();
        for ( HexTile blackTile : blackTiles ) {
            List<HexTile> adjacentTiles = findAdjacentTiles( blackTile );
            allAdjacentTiles.addAll( adjacentTiles );

            int blackCnt = 0;
            for ( HexTile adjacentTile : adjacentTiles ) {
                if ( blackTiles.contains( adjacentTile ) ) {
                    blackCnt++;
                }
            }

            if ( blackCnt == 1 || blackCnt == 2 ) {
                afterBlackTiles.add( blackTile );
            }
        }

        // Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black
        for ( HexTile tile : allAdjacentTiles ) {
            if ( ! blackTiles.contains( tile ) ) {
                List<HexTile> adjacentTiles = findAdjacentTiles( tile );

                int blackCnt = 0;
                for ( HexTile adjacentTile : adjacentTiles ) {
                    if ( blackTiles.contains( adjacentTile ) ) {
                        blackCnt++;
                    }
                }

                if ( blackCnt == 2 ) {
                    afterBlackTiles.add( tile );
                }
            }
        }

        return afterBlackTiles;
    }

    private HexTile followPathToTile( List<String> directions, HexTile fromTile ) {
        HexTile tile = new HexTile();
        if ( fromTile != null ) {
            tile.locX = fromTile.locX;
            tile.locY = fromTile.locY;
        }
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
            tile.locX += move[0];
            tile.locY += move[1];
        }
        tile.tileId = tile.locX + "-" + tile.locY;
        return tile;
    }

    private List<HexTile> findAdjacentTiles( HexTile tile ) {
        List<HexTile> adjacentTiles = new ArrayList<>();
        adjacentTiles.add( followPathToTile( Collections.singletonList( "e" ), tile ) );
        adjacentTiles.add( followPathToTile( Collections.singletonList( "w" ), tile ) );
        adjacentTiles.add( followPathToTile( Collections.singletonList( "ne" ), tile ) );
        adjacentTiles.add( followPathToTile( Collections.singletonList( "nw" ), tile ) );
        adjacentTiles.add( followPathToTile( Collections.singletonList( "se" ), tile ) );
        adjacentTiles.add( followPathToTile( Collections.singletonList( "sw" ), tile ) );
        return adjacentTiles;
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

class HexTile {
    String tileId;
    int locX;
    int locY;

    public int hashCode() {
        return tileId.hashCode();
    }

    public boolean equals( Object o ) {
        return (o instanceof HexTile) && tileId.equals( ((HexTile) o).tileId );
    }
}