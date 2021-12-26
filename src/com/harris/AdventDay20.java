package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay20 {

    public void executePart1() {
        System.out.println("ADVENT DAY 20 - PART 1...");
        //List<String> lines = new AdventDay20Test().testLines();
        List<String> lines = new InputReader().readStringInput("data-files/day20-input.txt" );

        Instant start = Instant.now();
        List<Tile> tiles = parseTiles( lines );
        analyzeForTileMatches( tiles );

        long answer = 1;
        for ( Tile tile : tiles ) {
            if ( tile.matches.size() == 2 ) {
                answer *= tile.tileId;
            }
        }

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer);
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 20 - PART 2...");
        List<String> lines = new AdventDay20Test().testLines();
        //List<String> lines = new InputReader().readStringInput("data-files/day20-input.txt" );

        Instant start = Instant.now();
        List<Tile> tiles = parseTiles( lines );
        analyzeForTileMatches( tiles );
        List<List<Tile>> image = arrangeTiles( tiles );

        for ( List<Tile> imageRow : image ) {
            for ( Tile imageCol : imageRow ) {
                System.out.print( "..." );
                System.out.print( imageCol == null ? "NULL" : imageCol.tileId );
            }
            System.out.println();
        }

        long answer = 1;
        for ( Tile tile : tiles ) {
            System.out.println( "Tile Id: " + tile.tileId + ", matches: " + tile.matches );
            if ( tile.matches.size() == 2 ) {
                answer *= tile.tileId;
            }
        }

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer);
    }

    private List<Tile> parseTiles( List<String> lines ) {
        List<Tile> tiles = new ArrayList<>();
        List<String> workRows = null;
        int workTileId = 0;
        for ( String line : lines ) {
            if ( line.startsWith( "Tile" ) ) {
                workRows = new ArrayList<>();
                workTileId = Integer.parseInt( line.split( "Tile " )[1].split( ":" )[0] );
            } else if ( line.trim().isEmpty() ) {
                if ( workRows != null ) {
                    tiles.add( new Tile( workTileId, workRows ) );
                    workRows = null;
                }
            } else {
                if ( workRows != null ) {
                    workRows.add( line.trim() );
                }
            }
        }
        if ( workRows != null ) {
            tiles.add( new Tile( workTileId, workRows ) );
        }
        return tiles;
    }

    private void analyzeForTileMatches( List<Tile> tiles ) {
        for ( Tile tileA : tiles ) {
            for ( Tile tileB : tiles ) {
                if ( tileA.tileId != tileB.tileId && ! tileA.comparedTiles.contains( tileB.tileId ) ) {
                    compareTiles( tileA, tileB );
                    tileA.comparedTiles.add( tileB.tileId );
                    tileB.comparedTiles.add( tileA.tileId );
                }
            }
        }
    }

    private void compareTiles( Tile tileA, Tile tileB ) {
        List<TileMatch> matches = tileA.matchSides( tileB );
        for ( TileMatch match : matches ) {
            tileA.matches.add( match );

            TileMatch matchB = new TileMatch();
            matchB.matchedTileId = tileA.tileId;
            matchB.tileSide = match.matchTileSide;
            matchB.matchTileSide = match.tileSide;
            tileB.matches.add( matchB );
        }
    }

    private List<List<Tile>> arrangeTiles( List<Tile> tiles ) {
        List<List<Tile>> image = new ArrayList<>();
        for ( Tile tile : tiles ) {
            placeTile( tile, image );
            for ( TileMatch match : tile.matches ) {
                for ( Tile matchTile : tiles ) {
                    if ( match.matchedTileId == matchTile.tileId ) {
                        placeTile( matchTile, image );
                        break;
                    }
                }
            }
        }
        return image;
    }

    private void placeTile( Tile tile, List<List<Tile>> image ) {
        int rowIdx = -1;
        int colIdx = -1;
        if ( ! image.isEmpty() ) {
            if ( findTileInImage( tile.tileId, image ) != null ) {
                // Just return...tile already placed
                return;
            }

            // Find the location of the first match
            for ( TileMatch match : tile.matches ) {
                int[] matchLoc = findTileInImage( match.matchedTileId, image );
                if ( matchLoc != null ) {
                    rowIdx = matchLoc[0];
                    colIdx = matchLoc[1];

                    if ( match.tileSide == 2 && match.matchTileSide == 4 ) {
                        colIdx -= 1;
                    } else if ( match.tileSide == 4 && match.matchTileSide == 2 ) {
                        colIdx += 1;
                    } else if ( match.tileSide == 1 && match.matchTileSide == 3 ) {
                        rowIdx += 1;
                    } else if ( match.tileSide == 3 && match.matchTileSide == 1 ) {
                        rowIdx -= 1;
                    } else {
                        rowIdx = -1;
                        colIdx = -1;
                    }
                    break;
                }
            }

            if ( rowIdx == -1 && colIdx == -1 ) {
                return;
            }
        }
        addTileToImage(tile, image, rowIdx, colIdx);
    }

    private void addTileToImage( Tile tile, List<List<Tile>> image, int rowIdx, int colIdx ) {
        List<Tile> row = null;
        if ( rowIdx < 0 ) {
            row = new ArrayList<>();
            image.add( 0, row );
        } else if ( rowIdx >= image.size() ) {
            for ( int i = image.size(); i <= rowIdx; i++ ) {
                row = new ArrayList<>();
                image.add( row );
            }
        } else {
            row = image.get( rowIdx );
        }

        if ( colIdx < 0 ) {
            row.add( 0 , tile );
        } else {
            if ( colIdx >= row.size() ) {
                for ( int i = row.size(); i <= colIdx; i++ ) {
                    row.add(null);
                }
            }
            row.set(colIdx, tile);
        }
    }

    private int[] findTileInImage( int tileId, List<List<Tile>> image ) {
        Integer rowIdx = null;
        Integer colIdx = null;
        int wrkRow = 0;
        int wrkCol;
        done:
        for ( List<Tile> row : image ) {
            wrkCol = 0;
            for ( Tile col : row ) {
                if ( col != null && tileId == col.tileId ) {
                    rowIdx = wrkRow;
                    colIdx = wrkCol;
                    break done;
                }
                wrkCol++;
            }
            wrkRow++;
        }

        if ( rowIdx != null ) {
            return new int[] { rowIdx, colIdx };
        } else {
            return null;
        }
    }

}

class Tile {
    int tileId;
    char[][] squares;
    int rotateFlipPoint = 1;
    Set<Integer> comparedTiles = new HashSet<>();
    List<TileMatch> matches = new ArrayList<>();

    Tile( int tileId, List<String> rows ) {
        this.tileId = tileId;
        squares = new char[rows.size()][rows.get(0).length()];
        for ( int i = 0; i < rows.size(); i++ ) {
            char[] rowSquares = rows.get( i ).toCharArray();
            System.arraycopy( rowSquares, 0, squares[i], 0, rowSquares.length );
        }
    }

    void rotateAndFlip( int toPoint ) {
        // Rotate till reaching desired point
        int toPointAbs = Math.abs( toPoint );
        int currPointAbs = Math.abs( rotateFlipPoint );
        while( currPointAbs != toPointAbs ) {
            char[][] rotateSquares = new char[squares.length][squares.length];
            for ( int i = 0, x = squares.length - 1; i < squares.length; i++, x-- ) {
                for ( int j = 0; j < squares[i].length; j++ ) {
                    rotateSquares[j][x] = squares[i][j];
                }
            }
            squares = rotateSquares;

            currPointAbs++;
            if ( currPointAbs > 3 ) {
                currPointAbs = 0;
            }
        }

        // Flip if necessary
        if ( ( rotateFlipPoint < 0 && toPoint > 0 ) || ( rotateFlipPoint > 0 && toPoint < 0 ) ) {
            char[][] flippedSquares = new char[squares.length][squares.length];
            for ( int i = 0; i < squares.length; i++ ) {
                for ( int j = 0, x = squares.length - 1; j < squares[i].length; j++, x-- ) {
                    flippedSquares[i][j] = squares[i][x];
                }
            }
            squares = flippedSquares;
        }

        rotateFlipPoint = toPoint;
    }

    List<TileMatch> matchSides( Tile tile ) {
        // 0,0...0,<LEN>         - Side 1
        // 0,<LEN>...<LEN>,<LEN> - Side 2
        // <LEN>,0...<LEN>,<LEN> - Side 3
        // 0,0...<LEN>,0         - Side 4
        // 0,<LEN>...0,0         - Side 1 FLIPPED
        // <LEN>,<LEN>...0,<LEN> - Side 2 FLIPPED
        // <LEN>,<LEN>...<LEN>,0 - Side 3 FLIPPED
        // <LEN>,0...0,0         - Side 4 FLIPPED
        int len = squares.length;
        int[][] combos = {
                {0,0,0,1,1}, {0,len-1,1,0,2}, {len-1,0,0,1,3}, {0,0,1,0,4},
                {0,len-1,0,-1,-1}, {len-1,len-1,-1,0,-2}, {len-1,len-1,0,-1,-3}, {len-1,0,-1,0,-4},
        };

        List<TileMatch> matches = new ArrayList<>();
        for (int[] aCombo : combos) {
            for (int[] bCombo : combos) {
                int aRow = aCombo[0];
                int aCol = aCombo[1];
                int aRowAdd = aCombo[2];
                int aColAdd = aCombo[3];
                int aSide = aCombo[4];
                int bRow = bCombo[0];
                int bCol = bCombo[1];
                int bRowAdd = bCombo[2];
                int bColAdd = bCombo[3];
                int bSide = bCombo[4];

                boolean prevCheck = false;
                for (TileMatch m : matches) {
                    if (m.tileSide == aSide * -1 && m.matchTileSide == bSide * -1) {
                        prevCheck = true;
                        break;
                    }
                }
                if (prevCheck) {
                    continue;
                }

                boolean match = true;
                for (int x = 0; x < len; x++) {
                    if (squares[aRow][aCol] != tile.squares[bRow][bCol]) {
                        match = false;
                        break;
                    }
                    aRow += aRowAdd;
                    aCol += aColAdd;
                    bRow += bRowAdd;
                    bCol += bColAdd;
                }

                if (match) {
                    TileMatch m = new TileMatch();
                    m.matchedTileId = tile.tileId;
                    m.tileSide = aSide;
                    m.matchTileSide = bSide;
                    matches.add(m);
                }
            }
        }

        return matches;
    }

    void print( String prefix ) {
        System.out.println( prefix + "Tile: " + tileId );
        for ( char[] row : squares ) {
            for ( char col : row ) {
                System.out.print( col );
            }
            System.out.println();
        }
        System.out.println();
    }

    public int hashCode() {
        return ((Integer) tileId).hashCode();
    }

    public boolean equals( Object o ) {
        return (o instanceof Tile) && ((Integer) tileId).equals( ((Tile) o).tileId );
    }
}

class TileMatch {
    int matchedTileId;
    int tileSide;
    int matchTileSide;
    public String toString() {
        return matchedTileId + "." + matchTileSide + "->" + tileSide;
    }
}

class AdventDay20Test {
    public List<String> testLines() {
        return java.util.Arrays.asList(
                "Tile 2311:",
                "..##.#..#.",
                "##..#.....",
                "#...##..#.",
                "####.#...#",
                "##.##.###.",
                "##...#.###",
                ".#.#.#..##",
                "..#....#..",
                "###...#.#.",
                "..###..###",
                "",
                "Tile 1951:",
                "#.##...##.",
                "#.####...#",
                ".....#..##",
                "#...######",
                ".##.#....#",
                ".###.#####",
                "###.##.##.",
                ".###....#.",
                "..#.#..#.#",
                "#...##.#..",
                "",
                "Tile 1171:",
                "####...##.",
                "#..##.#..#",
                "##.#..#.#.",
                ".###.####.",
                "..###.####",
                ".##....##.",
                ".#...####.",
                "#.##.####.",
                "####..#...",
                ".....##...",
                "",
                "Tile 1427:",
                "###.##.#..",
                ".#..#.##..",
                ".#.##.#..#",
                "#.#.#.##.#",
                "....#...##",
                "...##..##.",
                "...#.#####",
                ".#.####.#.",
                "..#..###.#",
                "..##.#..#.",
                "",
                "Tile 1489:",
                "##.#.#....",
                "..##...#..",
                ".##..##...",
                "..#...#...",
                "#####...#.",
                "#..#.#.#.#",
                "...#.#.#..",
                "##.#...##.",
                "..##.##.##",
                "###.##.#..",
                "",
                "Tile 2473:",
                "#....####.",
                "#..#.##...",
                "#.##..#...",
                "######.#.#",
                ".#...#.#.#",
                ".#########",
                ".###.#..#.",
                "########.#",
                "##...##.#.",
                "..###.#.#.",
                "",
                "Tile 2971:",
                "..#.#....#",
                "#...###...",
                "#.#.###...",
                "##.##..#..",
                ".#####..##",
                ".#..####.#",
                "#..#.#..#.",
                "..####.###",
                "..#.#.###.",
                "...#.#.#.#",
                "",
                "Tile 2729:",
                "...#.#.#.#",
                "####.#....",
                "..#.#.....",
                "....#..#.#",
                ".##..##.#.",
                ".#.####...",
                "####.#.#..",
                "##.####...",
                "##..#.##..",
                "#.##...##.",
                "",
                "Tile 3079:",
                "#.#.#####.",
                ".#..######",
                "..#.......",
                "######....",
                "####.#..#.",
                ".#...#.##.",
                "#.#####.##",
                "..#.###...",
                "..#.......",
                "..#.###..."
        );
    }
}
