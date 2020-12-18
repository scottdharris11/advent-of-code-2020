package com.harris;

import java.util.List;

public class AdventDay2 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 2 - PART 1..." );
        int pwdValidCnt = 0;
        List<String> inputData = new InputReader().readStringInput( "day2-input.txt" );
        for ( String input : inputData ) {
            PasswordRule rule = parseRule( input );
            int occurCnt = countCharOccursInString( rule.pwd, rule.ruleChar );
            if ( occurCnt >= rule.minOccurs && occurCnt <= rule.maxOccurs ) {
                pwdValidCnt++;
            }
        }
        System.out.println( "  Valid Passwords: " + pwdValidCnt );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 2 - PART 2..." );
        int pwdValidCnt = 0;
        List<String> inputData = new InputReader().readStringInput( "day2-input.txt" );
        for ( String input : inputData ) {
            PasswordRule rule = parseRule( input );
            int occurCnt = 0;
            if ( isCharAtIndex( rule.pwd, rule.ruleChar, rule.minOccurs ) ) {
                occurCnt++;
            }
            if ( isCharAtIndex( rule.pwd, rule.ruleChar, rule.maxOccurs ) ) {
                occurCnt++;
            }
            if ( occurCnt == 1 ) {
                pwdValidCnt++;
            }
        }
        System.out.println( "  Valid Passwords: " + pwdValidCnt );
    }

    private PasswordRule parseRule( String input ) {
        PasswordRule rule = new PasswordRule();
        String[] splitVals = input.split( " " );
        populateMinMaxOccurs( rule, splitVals[0] );
        populateRuleChar( rule, splitVals[1] );
        rule.pwd = splitVals[2];
        return rule;
    }

    private void populateMinMaxOccurs( PasswordRule rule, String minMaxInput ) {
        String[] minMaxSplit = minMaxInput.split( "-" );
        rule.minOccurs = Integer.parseInt( minMaxSplit[0] );
        rule.maxOccurs = Integer.parseInt( minMaxSplit[1] );
    }

    private void populateRuleChar( PasswordRule rule, String charRuleInput ) {
        rule.ruleChar = charRuleInput.charAt( 0 );
    }

    private int countCharOccursInString( String password, char searchChar ) {
        int occurs = 0;
        for ( char pChar : password.toCharArray() ) {
            if ( pChar == searchChar ) {
                occurs++;
            }
        }
        return occurs;
    }

    private boolean isCharAtIndex( String password, char searchChar, int index ) {
        char[] pwdChars = password.toCharArray();
        int searchIdx = index - 1;
        return searchIdx < pwdChars.length && searchChar == pwdChars[searchIdx];
    }
}

class PasswordRule {
    char ruleChar;
    int minOccurs;
    int maxOccurs;
    String pwd;
}