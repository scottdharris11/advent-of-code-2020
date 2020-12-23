package com.harris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventDay4 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 4 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "data-files/day4-input.txt" );
        List<Map<String,String>> passports = parsePassports( lines );

        String[] reqFields = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" };
        int validCnt = 0;
        for ( Map<String,String> passport : passports ) {
            if ( allFieldsExist( passport, reqFields ) ) {
                validCnt++;
            }
        }
        System.out.println( "  Valid passports: " + validCnt );
    }

    public void executePart2() {
        //byr (Birth Year) - four digits; at least 1920 and at most 2002.
        //iyr (Issue Year) - four digits; at least 2010 and at most 2020.
        //eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
        //hgt (Height) - a number followed by either cm or in:
        //If cm, the number must be at least 150 and at most 193.
        //If in, the number must be at least 59 and at most 76.
        //hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
        // ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
        // pid (Passport ID) - a nine-digit number, including leading zeroes.
        System.out.println( "ADVENT DAY 4 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "data-files/day4-input.txt" );
        List<Map<String,String>> passports = parsePassports( lines );

        String[] reqFields = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" };
        String[][] validations = {
                { "byr", "^([0-9]{4})$" },
                { "iyr", "^([0-9]{4})$" },
                { "eyr", "^([0-9]{4})$" },
                { "hgt", "^([0-9]+)(cm|in)$" },
                { "hcl", "^#[0-9a-f]{6}$" },
                { "ecl", "^(amb|blu|brn|gry|grn|hzl|oth)$" },
                { "pid", "^[0-9]{9}$" }
        };
        int validCnt = 0;
        for ( Map<String,String> passport : passports ) {
            if ( allFieldsExist( passport, reqFields ) &&
                 validateFieldPatterns( passport, validations ) &&
                 validateValueInRange( passport.get( "byr" ), 1920, 2002 ) &&
                 validateValueInRange( passport.get( "iyr" ), 2010, 2020 ) &&
                 validateValueInRange( passport.get( "eyr" ), 2020, 2030 ) &&
                 ( validateHeight( passport.get( "hgt" ), "cm", 150, 193 ) ||
                   validateHeight( passport.get( "hgt" ), "in", 59, 76 ) ) ) {
                validCnt++;
            }
        }
        System.out.println( "  Valid passports: " + validCnt );

    }

    private boolean validateHeight( String value, String ext, int min, int max ) {
        return value.endsWith( ext ) && validateValueInRange( value.substring( 0, value.length() - 2 ), min, max );
    }

    private boolean validateValueInRange( String value, int min, int max ) {
        int valueI = Integer.parseInt( value );
        return valueI >= min && valueI <= max;
    }

    private boolean validateFieldPatterns( Map<String,String> passport, String[][] validations ) {
        boolean allValid = true;
        for ( String[] validation : validations ) {
            String value = passport.get( validation[0] );
            if ( ! value.matches( validation[1] ) ) {
                allValid = false;
                break;
            }
        }
        return allValid;
    }

    private boolean allFieldsExist( Map<String,String> passport, String[] fields ) {
        boolean allExist = true;
        for ( String field : fields ) {
            if ( ! passport.containsKey( field ) ) {
                allExist = false;
                break;
            }
        }
        return allExist;
    }

    private List<Map<String,String>> parsePassports( List<String> lines ) {
        List<Map<String,String>> passports = new ArrayList<>();
        Map<String,String> passport = new HashMap<>();
        passports.add( passport );
        for ( String line : lines ) {
            if ( line.isEmpty() ) {
                passport = new HashMap<>();
                passports.add( passport );
            } else {
                addFieldsToPassport( passport, line );
            }
        }
        return passports;
    }

    private void addFieldsToPassport( Map<String,String> passport, String line ) {
        String[] fields = line.split( " " );
        for ( String field : fields ) {
            String[] values = field.split( ":" );
            passport.put( values[0], values[1] );
        }
    }

}
