package com.harris;

import java.util.*;

public class AdventDay6 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 6 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "day6-input.txt" );
        List<List<String>> groups = buildGroups( lines );
        List<Set<Character>> groupYesAnswers = new ArrayList<>();
        for ( List<String> group : groups ) {
            Set<Character> yesAnswers = new HashSet<>();
            for ( String member : group ) {
                for ( char answer : member.toCharArray() ) {
                    yesAnswers.add( answer );
                }
            }
            groupYesAnswers.add( yesAnswers );
        }

        int yesSum = 0;
        for ( Set<Character> answers : groupYesAnswers ) {
            yesSum += answers.size();
        }
        System.out.println( "  Yes Answer Sum: " + yesSum );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 6 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "day6-input.txt" );
        List<List<String>> groups = buildGroups( lines );
        List<Set<Character>> groupYesAnswers = new ArrayList<>();
        for ( List<String> group : groups ) {
            Set<Character> yesAnswers = new HashSet<>();
            boolean firstMember = true;
            for ( String member : group ) {
                if ( firstMember ) {
                    for (char answer : member.toCharArray()) {
                        yesAnswers.add(answer);
                    }
                    firstMember = false;
                } else {
                    List<Character> currAnswers = new ArrayList<>( yesAnswers );
                    for ( char yesAns : currAnswers ) {
                        boolean found = false;
                        for ( char answer : member.toCharArray() ) {
                            if ( answer == yesAns ) {
                                found = true;
                                break;
                            }
                        }
                        if ( ! found ) {
                            yesAnswers.remove( yesAns );
                        }
                    }
                }
            }
            groupYesAnswers.add( yesAnswers );
        }

        int yesSum = 0;
        for ( Set<Character> answers : groupYesAnswers ) {
            yesSum += answers.size();
        }
        System.out.println( "  Yes Answer Sum: " + yesSum );
    }

    private List<List<String>> buildGroups( List<String> lines ) {
        List<List<String>> groups = new ArrayList<>();
        List<String> group = new ArrayList<>();
        groups.add( group );
        for ( String line : lines ) {
            if ( line.isEmpty() ) {
                group = new ArrayList<>();
                groups.add( group );
            } else {
                group.add(line);
            }
        }
        return groups;
    }

}
