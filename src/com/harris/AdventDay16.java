package com.harris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventDay16 {

    public void executePart1() {
        System.out.println("ADVENT DAY 16 - PART 1...");
        /*List<String> lines = java.util.Arrays.asList(
            "class: 1-3 or 5-7",
            "row: 6-11 or 33-44",
            "seat: 13-40 or 45-50",
            " ",
            "your ticket:",
            "7,1,14",
            " ",
            "nearby tickets:",
            "7,3,47",
            "40,4,50",
            "55,2,20",
            "38,6,12"
        );*/
        List<String> lines = new InputReader().readStringInput("day16-input.txt" );

        List<Field> fields = new ArrayList<>();
        int ticketBegin = readFields( fields, lines );

        List<Ticket> tickets = new ArrayList<>();
        boolean processTickets = false;
        for ( String ticketLine : lines.subList( ticketBegin, lines.size() ) ) {
            if ( ticketLine.startsWith( "nearby tickets:" ) ) {
                processTickets = true;
            } else if ( processTickets ) {
                tickets.add( parseTicket( ticketLine ) );
            }
        }

        int ticketErrorRate = 0;
        for ( Ticket ticket : tickets ) {
            for ( int i = 0; i < ticket.values.length; i++ ) {
                boolean validVal = false;
                for ( Field field : fields ) {
                    for ( FieldRule rule : field.rules ) {
                        if (ticket.values[i] >= rule.minValue && ticket.values[i] <= rule.maxValue) {
                            validVal = true;
                            break;
                        }
                    }
                    if ( validVal ) {
                        break;
                    }
                }
                if ( ! validVal ) {
                    ticketErrorRate += ticket.values[i];
                }
            }
        }

        System.out.println( "  Ticket Error Rate: " + ticketErrorRate );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 16 - PART 2...");
        /*List<String> lines = java.util.Arrays.asList(
            "class: 0-1 or 4-19",
            "row: 0-5 or 8-19",
            "seat: 0-13 or 16-19",
            " ",
            "your ticket:",
            "11,12,13",
            " ",
            "nearby tickets:",
            "3,9,18",
            "15,1,5",
            "5,14,9"
        );*/
        List<String> lines = new InputReader().readStringInput("day16-input.txt" );

        List<Field> fields = new ArrayList<>();
        int ticketBegin = readFields( fields, lines );

        Ticket myTicket = parseTicket( lines.get( ticketBegin + 1 ) );

        List<Ticket> tickets = new ArrayList<>();
        boolean processTickets = false;
        for ( String ticketLine : lines.subList( ticketBegin, lines.size() ) ) {
            if ( ticketLine.startsWith( "nearby tickets:" ) ) {
                processTickets = true;
            } else if ( processTickets ) {
                tickets.add( parseTicket( ticketLine ) );
            }
        }

        List<Ticket> validTickets = discardInvalidTickets( tickets, fields );

        List<List<String>> potentialFields = new ArrayList<>();
        for ( int i = 0; i < myTicket.values.length; i++ ) {
            List<String> allValidFields = new ArrayList<>();
            for ( Field field : fields ) {
                boolean allValid = true;
                for ( Ticket ticket : validTickets ) {
                    boolean valid = false;
                    for ( FieldRule rule : field.rules ) {
                        if ( ticket.values[i] >= rule.minValue && ticket.values[i] <= rule.maxValue ) {
                            valid = true;
                            break;
                        }
                    }

                    if ( ! valid ) {
                        allValid = false;
                        break;
                    }
                }
                if ( allValid ) {
                    allValidFields.add( field.name );
                }
            }
            potentialFields.add( allValidFields );
        }

        while ( true ) {
            boolean allPotentialTo1Field = true;
            int fieldIdx = 0;
            for ( List<String> validFields : potentialFields ) {
                if ( validFields.size() == 1 ) {
                    String removeField = validFields.get( 0 );
                    int removeIdx = 0;
                    for ( List<String> removeFields : potentialFields ) {
                        if ( removeIdx != fieldIdx ) {
                            removeFields.remove( removeField );
                        }
                        removeIdx++;
                    }
                } else {
                    allPotentialTo1Field = false;
                }
                fieldIdx++;
            }

            if ( allPotentialTo1Field ) {
                break;
            }
        }

        int idx = 0;
        long answer = 1;
        for ( List<String> validFields : potentialFields ) {
            if ( validFields.get( 0 ).startsWith( "departure" ) ) {
                answer *= myTicket.values[idx];
            }
            idx++;
        }

        System.out.println( "  Departure Values: " + answer );
    }

    private List<Ticket> discardInvalidTickets( List<Ticket> tickets, List<Field> fields ) {
        List<Ticket> validTickets = new ArrayList<>();
        for ( Ticket ticket : tickets ) {
            boolean validTicket = true;
            for ( int i = 0; i < ticket.values.length; i++ ) {
                boolean validVal = false;
                for ( Field field : fields ) {
                    for ( FieldRule rule : field.rules ) {
                        if (ticket.values[i] >= rule.minValue && ticket.values[i] <= rule.maxValue) {
                            validVal = true;
                            break;
                        }
                    }
                    if ( validVal ) {
                        break;
                    }
                }
                if ( ! validVal ) {
                    validTicket = false;
                    break;
                }
            }
            if ( validTicket ) {
                validTickets.add( ticket );
            }
        }
        return validTickets;
    }

    private int readFields( List<Field> fields, List<String> lines ) {
        int index = -1;
        for ( String line : lines ) {
            index++;
            if ( line.trim().isEmpty() ) {
                index++;
                break;
            }

            String[] fieldEntries = line.split( ": " );
            Field field = new Field();
            field.name = fieldEntries[0];

            String[] ruleEntries = fieldEntries[1].split( " or " );
            for ( String ruleEntry : ruleEntries ) {
                String[] minMaxVals = ruleEntry.split( "-" );
                FieldRule rule = new FieldRule();
                rule.minValue = Integer.parseInt( minMaxVals[0] );
                rule.maxValue = Integer.parseInt( minMaxVals[1] );
                field.rules.add( rule );
            }

            fields.add( field );
        }
        return index;
    }

    private Ticket parseTicket( String line ) {
        String[] fieldStrVals = line.split( "," );
        int[] fieldVals = new int[fieldStrVals.length];
        for ( int i = 0; i < fieldStrVals.length; i++ ) {
            fieldVals[i] = Integer.parseInt( fieldStrVals[i] );
        }
        Ticket ticket = new Ticket();
        ticket.values = fieldVals;
        return ticket;
    }

}

class Field {
    String name;
    List<FieldRule> rules = new ArrayList<>();
}

class FieldRule {
    int minValue;
    int maxValue;
}

class Ticket {
    int[] values;
}
