package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay21 {

    public void executePart1() {
        System.out.println("ADVENT DAY 20 - PART 1...");
        /*List<String> lines = java.util.Arrays.asList(
                "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)",
                "trh fvjkl sbzzf mxmxvkd (contains dairy)",
                "sqjhc fvjkl (contains soy)",
                "sqjhc mxmxvkd sbzzf (contains fish)"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day21-input.txt" );

        Instant start = Instant.now();
        Map<String,Allergen> allergens = new HashMap<>();
        Map<String,Ingredient> ingredients = new HashMap<>();
        parseIngredients( lines, ingredients, allergens );

        long answer = 0;
        for ( Ingredient ingredient : ingredients.values() ) {
            if ( ingredient.possibleAllergens.isEmpty() ) {
                answer += ingredient.dishesUsed;
            }
        }

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer);
    }

    private void parseIngredients(
            List<String> lines, Map<String,Ingredient> ingredients, Map<String,Allergen> allergens ) {
        // With each line...
        //   For every NEW ingredient…record the possible allergens (if supplied, otherwise unknown)
        //   For every NEW allergen…record the possible ingredients
        //   For every Existing allergen…remove any possible ingredient not supplied
        //   For each ingredient removed from allergen list, remove allergen from possible allergens for those ingredients
        //   Don’t record allergen as possible on food if allergen is not NEW with dish
        for ( String line : lines ) {
            String[] parts = line.split( " \\(" );

            List<String> aEntries = new ArrayList<>();
            if ( parts.length > 1 ) {
                String[] aParts = parts[1].split( " " );
                for ( int i = 1; i < aParts.length; i++ ) {
                    aEntries.add( aParts[i]
                            .replaceAll( ",", "" )
                            .replaceAll( "\\)", "" ) );
                }
            }

            List<String> iEntries = new ArrayList<>();
            Collections.addAll( iEntries, parts[0].split( " " ) );

            for ( String iEntry : iEntries ) {
                Ingredient ing = ingredients.get( iEntry );
                if ( ing == null ) {
                    ing = new Ingredient();
                    ing.name = iEntry;

                    for ( String aEntry : aEntries ) {
                        if ( ! allergens.containsKey( aEntry ) ) {
                            ing.possibleAllergens.add( aEntry );
                        }
                    }
                    ingredients.put( ing.name, ing );
                }
                ing.dishesUsed++;
            }

            for ( String aEntry : aEntries ) {
                Allergen allergen = allergens.get( aEntry );
                if ( allergen == null ) {
                    allergen = new Allergen();
                    allergen.name = aEntry;
                    allergen.possibleIngredients.addAll( iEntries );
                    for ( String iEntry : iEntries ) {
                        ingredients.get( iEntry ).possibleAllergens.add( allergen.name );
                    }
                    allergens.put( allergen.name, allergen );
                } else {
                    List<String> removeIngs = new ArrayList<>();
                    for ( String ing : allergen.possibleIngredients ) {
                        if ( ! iEntries.contains( ing ) ) {
                            removeIngs.add( ing );
                        }
                    }

                    for ( String ing : removeIngs ) {
                        allergen.possibleIngredients.remove( ing );
                        ingredients.get( ing ).possibleAllergens.remove( allergen.name );
                    }
                }
            }
        }
    }

}

class Ingredient {
    String name;
    int dishesUsed;
    Set<String> possibleAllergens = new HashSet<>();
}

class Allergen {
    String name;
    Set<String> possibleIngredients = new HashSet<>();
}