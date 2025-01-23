package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay19 {

    public void executePart1() {
        System.out.println("ADVENT DAY 19 - PART 1...");
        /*List<String> lines = java.util.Arrays.asList(
                "0: 4 1 5",
                "1: 2 3 | 3 2",
                "2: 4 4 | 5 5",
                "3: 4 5 | 5 4",
                "4: \"a\"",
                "5: \"b\"",
                " ",
                "ababbb",
                "bababa",
                "abbbab",
                "aaabbb",
                "aaaabbb"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day19-input.txt" );

        Instant start = Instant.now();
        Map<Integer,RuleText> rules = new HashMap<>();
        int messageStartIdx = parseRules( rules, lines );
        expandRules( rules );

        //for ( RuleText rule : rules.values() ) {
        //    System.out.println( rule.ruleId +
        //            ": " + rule.origRule +
        //            ", regex: " + rule.baseRegex +
        //            ", expanded: " + rule.expandedRegex +
        //            ", numbers to expand: " + rule.rulesToExpand );
        //}

        int answer = 0;
        RuleText rule0 = rules.get( 0 );
        for ( int i = messageStartIdx; i < lines.size(); i++ ) {
            if ( lines.get( i ).matches( "^" + rule0.expandedRegex + "$" ) ) {
                //System.out.println( "Match found: " + lines.get( i ) );
                answer++;
            }
        }
        Instant end = Instant.now();
        System.out.println( "  [" + Duration.between( start, end ) + "] Answer: " + answer );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 19 - PART 2...");
        /*List<String> lines = java.util.Arrays.asList(
                "42: 9 14 | 10 1",
                "9: 14 27 | 1 26",
                "10: 23 14 | 28 1",
                "1: \"a\"",
                "11: 42 31",
                "5: 1 14 | 15 1",
                "19: 14 1 | 14 14",
                "12: 24 14 | 19 1",
                "16: 15 1 | 14 14",
                "31: 14 17 | 1 13",
                "6: 14 14 | 1 14",
                "2: 1 24 | 14 4",
                "0: 8 11",
                "13: 14 3 | 1 12",
                "15: 1 | 14",
                "17: 14 2 | 1 7",
                "23: 25 1 | 22 14",
                "28: 16 1",
                "4: 1 1",
                "20: 14 14 | 1 15",
                "3: 5 14 | 16 1",
                "27: 1 6 | 14 18",
                "14: \"b\"",
                "21: 14 1 | 1 14",
                "25: 1 1 | 1 14",
                "22: 14 14",
                "8: 42",
                "26: 14 22 | 1 20",
                "18: 15 15",
                "7: 14 5 | 1 21",
                "24: 14 1",
                " ",
                "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa",
                "bbabbbbaabaabba",
                "babbbbaabbbbbabbbbbbaabaaabaaa",
                "aaabbbbbbaaaabaababaabababbabaaabbababababaaa",
                "bbbbbbbaaaabbbbaaabbabaaa",
                "bbbababbbbaaaaaaaabbababaaababaabab",
                "ababaaaaaabaaab",
                "ababaaaaabbbaba",
                "baabbaaaabbaaaababbaababb",
                "abbbbabbbbaaaababbbbbbaaaababb",
                "aaaaabbaabaaaaababaa",
                "aaaabbaaaabbaaa",
                "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa",
                "babaaabbbaaabaababbaabababaaab",
                "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"
        );
        lines.set(4, "11: 42 31 | 42 42 31 31 | 42 42 42 31 31 31 | 42 42 42 42 31 31 31 31 | 42 42 42 42 42 31 31 31 31 31 | 42 42 42 42 42 42 31 31 31 31 31 31");
        lines.set(26, "8: 42 | 42 42 | 42 42 42 | 42 42 42 42 | 42 42 42 42 42 | 42 42 42 42 42 42 | 42 42 42 42 42 42 42");*/
        List<String> lines = new InputReader().readStringInput("data-files/day19-input.txt" );
        lines.set(15, "11: 42 31 | 42 42 31 31 | 42 42 42 31 31 31 | 42 42 42 42 31 31 31 31 | 42 42 42 42 42 31 31 31 31 31 | 42 42 42 42 42 42 31 31 31 31 31 31");
        lines.set(20, "8: 42 | 42 42 | 42 42 42 | 42 42 42 42 | 42 42 42 42 42 | 42 42 42 42 42 42 | 42 42 42 42 42 42 42");

        Instant start = Instant.now();
        Map<Integer,RuleText> rules = new HashMap<>();
        int messageStartIdx = parseRules( rules, lines );
        expandRules( rules );

        //for ( RuleText rule : rules.values() ) {
        //    System.out.println( rule.ruleId +
        //            ": " + rule.origRule +
        //            ", regex: " + rule.baseRegex +
        //            ", expanded: " + rule.expandedRegex +
        //            ", numbers to expand: " + rule.rulesToExpand );
        //}

        int answer = 0;
        RuleText rule0 = rules.get( 0 );
        for ( int i = messageStartIdx; i < lines.size(); i++ ) {
            if ( lines.get( i ).matches( "^" + rule0.expandedRegex + "$" ) ) {
                //System.out.println( "Match found: " + lines.get( i ) );
                answer++;
            }
        }
        Instant end = Instant.now();
        System.out.println( "  [" + Duration.between( start, end ) + "] Answer: " + answer );
    }

    private int parseRules( Map<Integer,RuleText> rules, List<String> lines ) {
        int messageStartIdx = 0;
        for ( String line : lines ) {
            if ( line.trim().length() == 0 ) {
                messageStartIdx++;
                break;
            } else {
                String[] entries = line.split( ": " );
                RuleText rule = new RuleText();
                rule.ruleId = Integer.parseInt( entries[0] );
                rule.origRule = entries[1];

                String[] regExEntries = entries[1].split( " \\| " );
                StringBuilder regex = new StringBuilder();
                if ( regExEntries.length > 1 ) {
                    regex.append( "(" );
                    boolean more = false;
                    for (String regExEntry : regExEntries) {
                        if ( more ) {
                            regex.append( "|" );
                        }
                        String[] matches = regExEntry.split( " " );
                        for ( String match : matches ) {
                            match = match.replaceAll( "\"", "" );
                            regex.append( " " );
                            regex.append( match );
                            regex.append( " " );

                            try {
                                rule.rulesToExpand.add( Integer.parseInt( match ) );
                            } catch ( NumberFormatException nfe ) {
                                // Ignoring
                            }
                        }
                        more = true;
                    }
                    regex.append( ")" );
                } else {
                    String[] matches = regExEntries[0].split( " " );
                    for ( String match : matches ) {
                        match = match.replaceAll( "\"", "" );
                        regex.append( " " );
                        regex.append( match );
                        regex.append( " " );

                        try {
                            rule.rulesToExpand.add( Integer.parseInt( match ) );
                        } catch ( NumberFormatException nfe ) {
                            // Ignoring
                        }
                    }
                }

                rule.baseRegex = regex.toString();
                rule.expandedRegex = rule.baseRegex;
                rules.put( rule.ruleId, rule );
            }
            messageStartIdx++;
        }

        return messageStartIdx;
    }

    private void expandRules( Map<Integer,RuleText> rules ) {
        boolean rulesToExpand = true;
        while ( rulesToExpand ) {
            rulesToExpand = false;
            for ( RuleText rule : rules.values() ) {
                if ( ! rule.expanded ) {
                    for ( Integer ruleId : rule.rulesToExpand.toArray( new Integer[]{} ) ) {
                        RuleText expandRule = rules.get( ruleId );
                        if ( expandRule.expanded ) {
                            rule.expandedRegex = rule.expandedRegex.replaceAll(
                                    "\\b" + ruleId + "\\b", expandRule.expandedRegex );
                            rule.rulesToExpand.remove( ruleId );
                        }
                    }

                    if ( rule.rulesToExpand.isEmpty() ) {
                        rule.expanded = true;
                        rule.expandedRegex = rule.expandedRegex.replaceAll( " ", "" );
                    } else {
                        rulesToExpand = true;
                    }
                }
            }
        }
    }

}

class RuleText {
    int ruleId;
    String origRule;
    String baseRegex;
    String expandedRegex;
    boolean expanded;
    Set<Integer> rulesToExpand = new HashSet<>();
    public String toString() {
        return ruleId +
                ": " + origRule +
                ", regex: " + baseRegex +
                ", expanded: " + expandedRegex +
                ", numbers to expand: " + rulesToExpand;
    }
}