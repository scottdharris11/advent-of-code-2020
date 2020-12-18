package com.harris;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventDay7 {

    public void executePart1() {
        System.out.println( "ADVENT DAY 7 - PART 1..." );
        List<String> lines = new InputReader().readStringInput( "day7-input.txt" );
        Map<String,Bag> bags = parseBags( lines );

        int matches = 0;
        String searchType = "shiny gold";
        for ( Bag bag : bags.values() ) {
            if ( ! bag.bagName.equals( searchType ) && checkBagTypeAllowed( bag, searchType ) ) {
                matches++;
            }
        }

        System.out.println( "  Bag count: " + matches );
    }

    public void executePart2() {
        System.out.println( "ADVENT DAY 7 - PART 2..." );
        List<String> lines = new InputReader().readStringInput( "day7-input.txt" );
        Map<String,Bag> bags = parseBags( lines );

        int reqBags = countRequiredBags( bags.get( "shiny gold" ) );

        System.out.println( "  Required Bags: " + reqBags );
    }

    private int countRequiredBags( Bag bag ) {
        int reqBags = 0;
        for ( Map.Entry<Bag,Integer> bagEntry : bag.allowedBags.entrySet() ) {
            reqBags += bagEntry.getValue();
            int subBags = countRequiredBags( bagEntry.getKey() );
            reqBags += ( subBags * bagEntry.getValue() );
        }
        return reqBags;
    }

    private boolean checkBagTypeAllowed( Bag bag, String bagType ) {
        boolean allowed = false;
        for ( Bag allowedBag : bag.allowedBags.keySet() ) {
            if ( allowedBag.bagName.equals( bagType ) ) {
                allowed = true;
                break;
            } else {
                allowed = checkBagTypeAllowed( allowedBag, bagType );
                if ( allowed ) {
                    break;
                }
            }
        }
        return allowed;
    }

    private Map<String,Bag> parseBags( List<String> lines ) {
        Map<String,Bag> bags = new HashMap<>();
        for ( String line : lines ) {
            String[] parts = line.split( " bags contain " );
            String bagName = parts[0];

            Bag bag = findCreateBag( bags, bagName );

            if ( ! parts[1].contains( "no other bags") ) {
                String[] allowedBags = parts[1].split( ", " );
                for ( String allowedBag : allowedBags ) {
                    parseAllowedBag( bag, allowedBag, bags );
                }
            }
        }
        return bags;
    }

    private Bag findCreateBag( Map<String,Bag> bags, String bagName ) {
        Bag bag = bags.get( bagName );
        if ( bag == null ) {
            bag = new Bag();
            bag.bagName = bagName;
            bags.put( bagName, bag );
        }
        return bag;
    }

    private void parseAllowedBag( Bag bag, String allowedBag, Map<String,Bag> bags ) {
        String countStr = allowedBag.substring( 0, allowedBag.indexOf( ' ' ) );
        String bagName = allowedBag.substring( allowedBag.indexOf( ' ' ) + 1, allowedBag.indexOf( " bag" ) );
        Bag childBag = findCreateBag( bags, bagName );
        bag.allowedBags.put( childBag, Integer.parseInt( countStr ) );
    }

}

class Bag {
    String bagName;
    Map<Bag,Integer> allowedBags = new HashMap<>();
}

