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
        //List<String> lines = new AdventDay20Test().testLines();
        List<String> lines = new InputReader().readStringInput("data-files/day20-input.txt" );

        Instant start = Instant.now();
        List<Tile> tiles = parseTiles( lines );
        analyzeForTileMatches( tiles );
        Image image = new Image(arrangeTiles( tiles ));
        //image.print();

        long answer = image.waves();
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
        Map<Integer,Tile> tilesById = new HashMap<>();
        Tile initialCorner = null;
        for ( Tile tile : tiles ) {
            if ( initialCorner == null && tile.matches.size() == 2 ) {
                initialCorner = tile;
            }
            tilesById.put(tile.tileId, tile);
        }
        Set<Integer> placed = new HashSet<>();
        tileToImage(image, initialCorner, 0, null, placed, tilesById);
        return image;
    }

    private static final Map<String,Integer[]> ROTATE_FLIPS = new HashMap<>();
    static {
        // keys represent the match tile sides
        // values represent:
        // - rotate times (always clockwise)
        // - flip (-1 == vertical, 0 == none, 1 == horizontal)
        ROTATE_FLIPS.put("1,1", new Integer[]{0,-1});
        ROTATE_FLIPS.put("1,-1", new Integer[]{2,0});
        ROTATE_FLIPS.put("1,2", new Integer[]{1,1});
        ROTATE_FLIPS.put("1,-2", new Integer[]{1,0});
        ROTATE_FLIPS.put("1,3", new Integer[]{0,0});
        ROTATE_FLIPS.put("1,-3", new Integer[]{0,1});
        ROTATE_FLIPS.put("1,4", new Integer[]{3,0});
        ROTATE_FLIPS.put("1,-4", new Integer[]{3,1});

        ROTATE_FLIPS.put("-1,1", new Integer[]{2,0});
        ROTATE_FLIPS.put("-1,-1", new Integer[]{0,-1});
        ROTATE_FLIPS.put("-1,2", new Integer[]{1,0});
        ROTATE_FLIPS.put("-1,-2", new Integer[]{1,1});
        ROTATE_FLIPS.put("-1,3", new Integer[]{0,1});
        ROTATE_FLIPS.put("-1,-3", new Integer[]{0,0});
        ROTATE_FLIPS.put("-1,4", new Integer[]{3,1});
        ROTATE_FLIPS.put("-1,-4", new Integer[]{3,0});

        ROTATE_FLIPS.put("2,1", new Integer[]{3,-1});
        ROTATE_FLIPS.put("2,-1", new Integer[]{3,0});
        ROTATE_FLIPS.put("2,2", new Integer[]{0,1});
        ROTATE_FLIPS.put("2,-2", new Integer[]{2,0});
        ROTATE_FLIPS.put("2,3", new Integer[]{1,0});
        ROTATE_FLIPS.put("2,-3", new Integer[]{1,-1});
        ROTATE_FLIPS.put("2,4", new Integer[]{0,0});
        ROTATE_FLIPS.put("2,-4", new Integer[]{0,-1});

        ROTATE_FLIPS.put("-2,1", new Integer[]{3,0});
        ROTATE_FLIPS.put("-2,-1", new Integer[]{3,-1});
        ROTATE_FLIPS.put("-2,2", new Integer[]{2,0});
        ROTATE_FLIPS.put("-2,-2", new Integer[]{0,1});
        ROTATE_FLIPS.put("-2,3", new Integer[]{1,-1});
        ROTATE_FLIPS.put("-2,-3", new Integer[]{1,0});
        ROTATE_FLIPS.put("-2,4", new Integer[]{0,-1});
        ROTATE_FLIPS.put("-2,-4", new Integer[]{0,0});

        ROTATE_FLIPS.put("3,1", new Integer[]{0,0});
        ROTATE_FLIPS.put("3,-1", new Integer[]{0,1});
        ROTATE_FLIPS.put("3,2", new Integer[]{3,0});
        ROTATE_FLIPS.put("3,-2", new Integer[]{3,1});
        ROTATE_FLIPS.put("3,3", new Integer[]{0,-1});
        ROTATE_FLIPS.put("3,-3", new Integer[]{2,0});
        ROTATE_FLIPS.put("3,4", new Integer[]{1,1});
        ROTATE_FLIPS.put("3,-4", new Integer[]{1,0});

        ROTATE_FLIPS.put("-3,1", new Integer[]{0,1});
        ROTATE_FLIPS.put("-3,-1", new Integer[]{0,0});
        ROTATE_FLIPS.put("-3,2", new Integer[]{3,1});
        ROTATE_FLIPS.put("-3,-2", new Integer[]{3,0});
        ROTATE_FLIPS.put("-3,3", new Integer[]{2,0});
        ROTATE_FLIPS.put("-3,-3", new Integer[]{0,-1});
        ROTATE_FLIPS.put("-3,4", new Integer[]{1,0});
        ROTATE_FLIPS.put("-3,-4", new Integer[]{1,1});

        ROTATE_FLIPS.put("4,1", new Integer[]{1,0});
        ROTATE_FLIPS.put("4,-1", new Integer[]{1,-1});
        ROTATE_FLIPS.put("4,2", new Integer[]{0,0});
        ROTATE_FLIPS.put("4,-2", new Integer[]{0,-1});
        ROTATE_FLIPS.put("4,3", new Integer[]{3,-1});
        ROTATE_FLIPS.put("4,-3", new Integer[]{3,0});
        ROTATE_FLIPS.put("4,4", new Integer[]{0,1});
        ROTATE_FLIPS.put("4,-4", new Integer[]{2,0});

        ROTATE_FLIPS.put("-4,1", new Integer[]{1,-1});
        ROTATE_FLIPS.put("-4,-1", new Integer[]{1,0});
        ROTATE_FLIPS.put("-4,2", new Integer[]{0,-1});
        ROTATE_FLIPS.put("-4,-2", new Integer[]{0,0});
        ROTATE_FLIPS.put("-4,3", new Integer[]{3,0});
        ROTATE_FLIPS.put("-4,-3", new Integer[]{3,-1});
        ROTATE_FLIPS.put("-4,4", new Integer[]{2,0});
        ROTATE_FLIPS.put("-4,-4", new Integer[]{0,1});
    }

    private Integer[] findTile(List<List<Tile>> image, int tileId) {
        int tileRow = 0;
        for (List<Tile> row : image) {
            int tileCol = 0;
            for (Tile tile : row) {
                if (tile != null && tile.tileId == tileId) {
                    return new Integer[]{tileRow, tileCol};
                }
                tileCol++;
            }
            tileRow++;
        }
        return null;
    }

    private void tileToImage( List<List<Tile>> image, Tile tile, int prevTileId, TileMatch match, Set<Integer> placed, Map<Integer,Tile> tiles ) {
        // return if tile already placed
        if ( placed.contains(tile.tileId) ){
            return;
        }
        placed.add(tile.tileId);

        // determine where to place the tile relative to the last tile placed
        int row = 0;
        int col = 0;
        if ( match != null ) {
            Integer[] coords = findTile(image, prevTileId);
            row = coords[0];
            col = coords[1];
            //System.out.println("Prev Row/Col: " + row + "," + col + " (" + prevTileId + ") -> Match: " + match);
            switch (match.tileSide) {
                case 1:
                case -1:
                    row--;
                    break;
                case 2:
                case -2:
                    col++;
                    break;
                case 3:
                case -3:
                    row++;
                    break;
                case 4:
                case -4:
                    col--;
                    break;
            }
            String key = match.tileSide + "," + match.matchTileSide;
            Integer[] rotateFlip = ROTATE_FLIPS.get(key);
            //System.out.println("Rotate/Flip: " + rotateFlip[0] + ", " + rotateFlip[1]);
            tile.rotateAndFlip(rotateFlip[0], rotateFlip[1]);
        }
        addTileToImage(tile, image, row, col);
        //printImage(image, tile.squares.length, tile.tileId);
        if (row < 0) {
            row = 0;
        }
        if (col < 0) {
            col = 0;
        }

        // recurse to next tiles
        for ( TileMatch next : tile.matches ) {
            tileToImage(image, tiles.get(next.matchedTileId), tile.tileId, next, placed, tiles);
        }
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
            if (row.get(colIdx) != null) {
                System.out.println("Overlay occurring at row/col: " + rowIdx + "," + colIdx + " with tile " + tile.tileId);
                System.exit(1);
            }
            row.set(colIdx, tile);
        }
    }

    void printImage(List<List<Tile>> tiles, int squaresPerTile, int tileId) {
        for (List<Tile> row : tiles) {
            for (int r = 0; r < squaresPerTile; r++) {
                for (Tile tile : row) {
                    for (int j = 0; j < squaresPerTile; j++) {
                        if (tile == null) {
                            System.out.print(" ");
                        } else {
                            if (tile.tileId == tileId && tile.squares[r][j] == '.') {
                                System.out.print("*");
                            } else {
                                System.out.print(tile.squares[r][j]);
                            }
                        }
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

}

class Tile {
    int tileId;
    char[][] squares;
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

    void rotateAndFlip( int rotateTimes, int flip ) {
        // Rotate till reaching desired point
        for (int r = 0; r < rotateTimes; r++) {
            char[][] rotateSquares = new char[squares.length][squares.length];
            for ( int i = 0, x = squares.length - 1; i < squares.length; i++, x-- ) {
                for ( int j = 0; j < squares[i].length; j++ ) {
                    rotateSquares[j][x] = squares[i][j];
                }
            }
            squares = rotateSquares;
        }
        for (TileMatch m : matches) {
            int s = Math.abs(m.tileSide);
            s += rotateTimes;
            if (s > 4) {
                s -= 4;
            }
            if (m.tileSide >= -2 && m.tileSide <= 2 && s > 2) {
                if (m.tileSide > 0) {
                    s *= -1;
                }
            } else if ((m.tileSide < -2 || m.tileSide > 2) && s < 3) {
                if (m.tileSide > 0) {
                    s *= -1;
                }
            } else {
                if (m.tileSide < 0) {
                    s *= -1;
                }
            }
            m.tileSide = s;
        }

        if (flip == 1) {
            // Flip horizontal
            char[][] flippedSquares = new char[squares.length][squares.length];
            for ( int i = 0; i < squares.length; i++ ) {
                for ( int j = 0, x = squares.length - 1; j < squares[i].length; j++, x-- ) {
                    flippedSquares[i][j] = squares[i][x];
                }
            }
            squares = flippedSquares;
            for (TileMatch m : matches) {
                if (m.tileSide % 2 == 0) {
                    int s = Math.abs(m.tileSide);
                    s += 2;
                    if (s > 4) {
                        s -= 4;
                    }
                    if (m.tileSide < 0) {
                        s *= -1;
                    }
                    m.tileSide = s;
                } else {
                    m.tileSide *= -1;
                }
            }
        } else if (flip == -1) {
            // Flip vertical
            char[][] flippedSquares = new char[squares.length][squares.length];
            for ( int i = 0, x = squares.length - 1; i < squares.length; i++, x-- ) {
                for ( int j = 0; j < squares[i].length; j++ ) {
                    flippedSquares[i][j] = squares[x][j];
                }
            }
            squares = flippedSquares;
            for (TileMatch m : matches) {
                if (m.tileSide % 2 == 0) {
                    m.tileSide *= -1;
                } else {
                    int s = Math.abs(m.tileSide);
                    s += 2;
                    if (s > 4) {
                        s -= 4;
                    }
                    if (m.tileSide < 0) {
                        s *= -1;
                    }
                    m.tileSide = s;
                }
            }
        }
    }

    List<TileMatch> matchSides( Tile tile ) {
        // 0,0...0,<LEN>         - Side 1 (top from left)
        // 0,<LEN>...<LEN>,<LEN> - Side 2 (right from top)
        // <LEN>,0...<LEN>,<LEN> - Side 3 (bottom from left)
        // 0,0...<LEN>,0         - Side 4 (left from top)
        // 0,<LEN>...0,0         - Side 1 FLIPPED (top from right)
        // <LEN>,<LEN>...0,<LEN> - Side 2 FLIPPED (right from bottom)
        // <LEN>,<LEN>...<LEN>,0 - Side 3 FLIPPED (bottom from right)
        // <LEN>,0...0,0         - Side 4 FLIPPED (left from bottom)
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

class Image {
    Integer[][][] monsterPatterns = new Integer[][][] {
            {{0,0},{1,1},{4,1},{5,0},{6,0},{7,1},{10,1},{11,0},{12,0},{13,1},{16,1},{17,0},{18,0},{18,-1},{19,0}},
            {{0,0},{1,-1},{4,-1},{5,0},{6,0},{7,-1},{10,-1},{11,0},{12,0},{13,-1},{16,-1},{17,0},{18,0},{18,1},{19,0}},
            {{0,0},{-1,1},{-4,1},{-5,0},{-6,0},{-7,1},{-10,1},{-11,0},{-12,0},{-13,1},{-16,1},{-17,0},{-18,0},{-18,-1},{-19,0}},
            {{0,0},{-1,-1},{-4,-1},{-5,0},{-6,0},{-7,-1},{-10,-1},{-11,0},{-12,0},{-13,-1},{-16,-1},{-17,0},{-18,0},{-18,1},{-19,0}},
    };
    char[][] pixels;
    int waves;
    Image(List<List<Tile>> tiles) {
        int pixelsPerTile = tiles.get(0).get(0).squares.length-2;
        int len = tiles.size() * pixelsPerTile;
        this.pixels = new char[len][len];
        int yBegin = 0;
        for (List<Tile> row : tiles) {
            int xBegin = 0;
            for (Tile tile : row) {
                int yOffset = yBegin;
                for (int i = 1; i < tile.squares.length-1; i++) {
                    int xOffset = xBegin;
                    for (int j = 1; j < tile.squares.length-1; j++) {
                        this.pixels[yOffset][xOffset] = tile.squares[i][j];
                        if (tile.squares[i][j] == '#') {
                            this.waves++;
                        }
                        xOffset++;
                    }
                    yOffset++;
                }
                xBegin += pixelsPerTile;
            }
            yBegin += pixelsPerTile;
        }
    }

    void print() {
        for ( char[] row : pixels ) {
            for ( char col : row ) {
                System.out.print( col );
            }
            System.out.println();
        }
    }

    int waves() {
        int monsters = 0;
        int len = pixels.length;
        for (int y = 0; y < len; y++) {
            for (int x = 0; x < len; x++) {
                if (isSeaMonster(x, y)) {
                    //System.out.println("Monster at " + x + "," + y);
                    monsters++;
                }
            }
        }
        return waves - (monsters * monsterPatterns[0].length);
    }

    boolean isSeaMonster(int x, int y) {
        // check x/y patterns
        int isize = pixels.length;
        for (int m = 0; m < monsterPatterns.length; m++) {
            boolean monster = true;
            for (int p = 0; p < monsterPatterns[m].length; p++) {
                int ix = x + monsterPatterns[m][p][0];
                int iy = y + monsterPatterns[m][p][1];
                if (ix < 0 || iy < 0 || ix >= isize || iy >= isize || pixels[iy][ix] != '#') {
                    monster = false;
                    break;
                }
            }
            if (monster) {
                return true;
            }
        }

        // check y/x patterns
        for (int m = 0; m < monsterPatterns.length; m++) {
            boolean monster = true;
            for (int p = 0; p < monsterPatterns[m].length; p++) {
                int iy = y + monsterPatterns[m][p][0];
                int ix = x + monsterPatterns[m][p][1];
                if (ix < 0 || iy < 0 || ix >= isize || iy >= isize || pixels[iy][ix] != '#') {
                    monster = false;
                    break;
                }
            }
            if (monster) {
                return true;
            }
        }

        return false;
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
