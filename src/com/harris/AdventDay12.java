package com.harris;

import java.util.List;
import java.util.stream.Collectors;

public class AdventDay12 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 12 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "day12-input.txt" );
        //List<String> lines = Arrays.asList( "F10", "N3", "F7", "R90", "F11" );
        List<ShipCommand> commands = parseCommands( lines );
        ShipPosition position = new ShipPosition();
        for ( ShipCommand command : commands ) {
            position.executeCommand( command );
        }
        int manhattanDistance = Math.abs( position.eastWestPos ) + Math.abs( position.northSouthPos );
        System.out.println( "  Manhattan Distance from origin: " + manhattanDistance );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 12 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "day12-input.txt" );
        //List<String> lines = Arrays.asList( "F10", "N3", "F7", "R90", "F11" );
        List<ShipCommand> commands = parseCommands( lines );
        ShipPosition2 position = new ShipPosition2();
        for ( ShipCommand command : commands ) {
            position.executeCommand( command );
        }
        int manhattanDistance = Math.abs( position.eastWestPos ) + Math.abs( position.northSouthPos );
        System.out.println( "  Manhattan Distance from origin: " + manhattanDistance );
    }

    private List<ShipCommand> parseCommands( List<String> lines ) {
        return lines.stream().map(line -> {
            ShipCommand command = new ShipCommand();
            command.command = line.toCharArray()[0];
            command.amount = Integer.parseInt( line.substring( 1) );
            return command;
        }).collect( Collectors.toList() );
    }

}

class WayPoint {
    int eastWestOffset = 10;
    int northSouthOffset = 1;
}

class ShipCommand {
    static final char NORTH = 'N';
    static final char SOUTH = 'S';
    static final char EAST  = 'E';
    static final char WEST  = 'W';
    static final char LEFT  = 'L';
    static final char RIGHT = 'R';
    static final char FORWARD = 'F';
    char command;
    int amount;
}

class ShipPosition {
    int eastWestPos = 0;
    int northSouthPos = 0;
    char currentDir = 'E';

    void executeCommand( ShipCommand command ) {
        switch ( command.command ) {
            case ShipCommand.EAST:
                move( ShipCommand.EAST, command.amount );
                break;
            case ShipCommand.WEST:
                move( ShipCommand.WEST, command.amount );
                break;
            case ShipCommand.NORTH:
                move( ShipCommand.NORTH, command.amount );
                break;
            case ShipCommand.SOUTH:
                move( ShipCommand.SOUTH, command.amount );
                break;
            case ShipCommand.LEFT:
                adjustDirection( -1, command.amount );
                break;
            case ShipCommand.RIGHT:
                adjustDirection( 1, command.amount );
                break;
            case ShipCommand.FORWARD:
                move( currentDir, command.amount );
                break;
            default:
                break;
        }
    }

    private void move( char direction, int amount ) {
        switch ( direction ) {
            case ShipCommand.EAST:
                eastWestPos += amount;
                break;
            case ShipCommand.WEST:
                eastWestPos -= amount;
                break;
            case ShipCommand.NORTH:
                northSouthPos += amount;
                break;
            case ShipCommand.SOUTH:
                northSouthPos -= amount;
                break;
            default:
                break;
        }
    }

    private void adjustDirection( int direction, int degrees ) {
        char[] directions = { ShipCommand.EAST, ShipCommand.SOUTH, ShipCommand.WEST, ShipCommand.NORTH };
        int currDirIdx = 0;
        for ( int i = 0; i < directions.length; i++ ) {
            if ( currentDir == directions[i] ) {
                currDirIdx = i;
                break;
            }
        }

        int moves = (int) Math.floor( degrees / 90.0 );
        for ( int i = 0; i < moves; i++ ) {
            currDirIdx += direction;
            if ( currDirIdx < 0 ) {
                currDirIdx = directions.length - 1;
            }
            if ( currDirIdx == directions.length ) {
                currDirIdx = 0;
            }
            currentDir = directions[currDirIdx];
        }
    }

}

class ShipPosition2 {
    WayPoint wayPoint = new WayPoint();
    int eastWestPos = 0;
    int northSouthPos = 0;

    void executeCommand( ShipCommand command ) {
        switch ( command.command ) {
            case ShipCommand.EAST:
                moveWayPoint( wayPoint, ShipCommand.EAST, command.amount );
                break;
            case ShipCommand.WEST:
                moveWayPoint( wayPoint, ShipCommand.WEST, command.amount );
                break;
            case ShipCommand.NORTH:
                moveWayPoint( wayPoint, ShipCommand.NORTH, command.amount );
                break;
            case ShipCommand.SOUTH:
                moveWayPoint( wayPoint, ShipCommand.SOUTH, command.amount );
                break;
            case ShipCommand.LEFT:
                adjustWayPoint( wayPoint, -1, command.amount );
                break;
            case ShipCommand.RIGHT:
                adjustWayPoint( wayPoint, 1, command.amount );
                break;
            case ShipCommand.FORWARD:
                moveShipByWayPoint( wayPoint, command.amount );
                break;
            default:
                break;
        }
    }

    private void moveShipByWayPoint( WayPoint wayPoint, int times ) {
        for ( int i = 0; i < times; i++ ) {
            eastWestPos += wayPoint.eastWestOffset;
            northSouthPos += wayPoint.northSouthOffset;
        }
    }

    private void moveWayPoint( WayPoint wayPoint, char direction, int amount ) {
        switch ( direction ) {
            case ShipCommand.EAST:
                wayPoint.eastWestOffset += amount;
                break;
            case ShipCommand.WEST:
                wayPoint.eastWestOffset -= amount;
                break;
            case ShipCommand.NORTH:
                wayPoint.northSouthOffset += amount;
                break;
            case ShipCommand.SOUTH:
                wayPoint.northSouthOffset -= amount;
                break;
            default:
                break;
        }
    }

    private void adjustWayPoint( WayPoint wayPoint, int direction, int degrees ) {
        int moves = (int) Math.floor( degrees / 90.0 );
        for ( int i = 0; i < moves; i++ ) {
            int ewOffset = wayPoint.eastWestOffset;
            int nsOffset = wayPoint.northSouthOffset;
            wayPoint.eastWestOffset = nsOffset * direction;
            wayPoint.northSouthOffset = ewOffset * direction * -1;
        }
    }

}
