package com.harris;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdventDay8 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 8 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "day8-input.txt" );
        List<BootCommand> commands = parseCommands( lines );
        int value = executeCommands( commands, false );
        System.out.println( "  Value at loop start: " + value );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 8 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "day8-input.txt" );
        List<BootCommand> commands = parseCommands( lines );
        int cmdIdx = 0;
        int fixValue = -1;
        for ( BootCommand command : commands ) {
            if ( command.command.equals( "nop" ) ) {
                fixValue = flipCommandAndExecute( commands, cmdIdx, "nop", "jmp" );
            } else if ( command.command.equals( "jmp" ) ) {
                fixValue = flipCommandAndExecute( commands, cmdIdx, "jmp", "nop" );
            }

            if ( fixValue > -1 ) {
                break;
            }
            cmdIdx++;
        }
        System.out.println( "  Value with fix: " + fixValue );
    }

    private int flipCommandAndExecute( List<BootCommand> commands, int flipIdx, String fromCmd, String toCmd ) {
        BootCommand command = commands.get( flipIdx );
        command.command = toCmd;
        int exeValue = executeCommands( commands, true );
        command.command = fromCmd;
        return exeValue;
    }

    private int executeCommands( List<BootCommand> commands, boolean indicateLoop ) {
        int value = 0;
        Set<Integer> executedIndexes = new HashSet<>();
        int exeIdx = 0;
        boolean looping = false;
        while ( exeIdx < commands.size() ) {
            if ( executedIndexes.contains( exeIdx ) ) {
                looping = true;
                break;
            }
            executedIndexes.add( exeIdx );

            BootCommand command = commands.get( exeIdx );
            switch (command.command) {
                case "nop":
                    exeIdx++;
                    break;
                case "acc":
                    value += command.amount;
                    exeIdx++;
                    break;
                case "jmp":
                    exeIdx += command.amount;
                    break;
            }
        }

        if ( looping && indicateLoop ) {
            value = -1;
        }
        return value;
    }

    private List<BootCommand> parseCommands( List<String> lines ) {
        List<BootCommand> commands = new ArrayList<>();
        for ( String line : lines ) {
            String[] parts = line.split( " " );
            BootCommand command = new BootCommand();
            command.command = parts[0];
            command.amount = Integer.parseInt( parts[1] );
            commands.add( command );
        }
        return commands;
    }

}

class BootCommand {
    String command;
    int amount;
}
