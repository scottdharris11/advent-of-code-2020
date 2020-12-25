package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay22 {

    public void executePart1() {
        System.out.println("ADVENT DAY 22 - PART 1...");
        /*List<String> lines = java.util.Arrays.asList(
                "Player 1:",
                "9",
                "2",
                "6",
                "3",
                "1",
                "",
                "Player 2:",
                "5",
                "8",
                "4",
                "7",
                "10"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day22-input.txt" );

        Instant start = Instant.now();

        List<Queue<Integer>> playerDecks = parseDecks( lines );
        int winnerIdx = playGame( playerDecks );
        Queue<Integer> winnerDeck = playerDecks.get( winnerIdx );
        long answer = buildDeckValue( winnerDeck );

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer);
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 22 - PART 2...");
        /*List<String> lines = java.util.Arrays.asList(
                "Player 1:",
                "9",
                "2",
                "6",
                "3",
                "1",
                "",
                "Player 2:",
                "5",
                "8",
                "4",
                "7",
                "10"
        );*/
        /*List<String> lines = java.util.Arrays.asList(
                "Player 1:",
                "43",
                "19",
                "",
                "Player 2:",
                "2",
                "29",
                "14"
        );*/
        List<String> lines = new InputReader().readStringInput("data-files/day22-input.txt" );

        Instant start = Instant.now();


        List<Queue<Integer>> playerDecks = parseDecks( lines );
        int winnerIdx = playGameRecursive( playerDecks );
        Queue<Integer> winnerDeck = playerDecks.get( winnerIdx );
        long answer = buildDeckValue( winnerDeck );

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] WRONG Answer: " + answer);
    }

    private int playGame( List<Queue<Integer>> playerDecks ) {
        boolean gameOn = true;
        int roundWinnerIdx = Integer.MIN_VALUE;
        while ( gameOn ) {
            int roundWinnerCard = Integer.MIN_VALUE;
            List<Integer> roundCards = new ArrayList<>();
            for ( int i = 0; i < playerDecks.size(); i++ ) {
                Queue<Integer> playerDeck = playerDecks.get( i );
                int roundCard = playerDeck.poll();
                roundCards.add( roundCard );
                if ( roundCard > roundWinnerCard ) {
                    roundWinnerCard = roundCard;
                    roundWinnerIdx = i;
                }
            }

            Queue<Integer> winnerDeck = playerDecks.get( roundWinnerIdx );
            winnerDeck.add( roundCards.get( roundWinnerIdx ) );
            roundCards.remove( roundWinnerIdx );
            winnerDeck.addAll( roundCards );

            gameOn = false;
            for ( int i = 0; i < playerDecks.size(); i++ ) {
                if ( i != roundWinnerIdx ) {
                    Queue<Integer> playerDeck = playerDecks.get( i );
                    if ( ! playerDeck.isEmpty() ) {
                        gameOn = true;
                        break;
                    }
                }
            }
        }
        return roundWinnerIdx;
    }

    private int playGameRecursive( List<Queue<Integer>> playerDecks ) {
        boolean gameOn = true;
        int roundWinnerIdx = Integer.MIN_VALUE;
        Set<Long> prevRoundsVals = new HashSet<>();
        while ( gameOn ) {
            // Before either player deals a card, if there was a previous round
            // in this game that had exactly the same cards in the same order
            // in the same players' decks, the game instantly ends in a win for player 1.
            long roundVal = 0;
            for ( int i = 1; i <= playerDecks.size(); i++ ) {
                roundVal += ( buildDeckValue( playerDecks.get( i-1) ) * i );
            }
            if ( prevRoundsVals.contains( roundVal ) ) {
                roundWinnerIdx = 0;
                break;
            } else {
                prevRoundsVals.add( roundVal );
            }

            // Otherwise, this round's cards must be in a new configuration;
            // the players begin the round by each drawing the top card of their deck as normal
            int roundWinnerCard = Integer.MIN_VALUE;
            List<Integer> roundCards = new ArrayList<>();
            boolean subGame = true;
            for ( int i = 0; i < playerDecks.size(); i++ ) {
                Queue<Integer> playerDeck = playerDecks.get( i );
                int roundCard = playerDeck.poll();
                roundCards.add( roundCard );
                if ( roundCard > roundWinnerCard ) {
                    roundWinnerCard = roundCard;
                    roundWinnerIdx = i;
                }
                if ( roundCard > playerDeck.size() ) {
                    subGame = false;
                }
            }

            // If both players have at least as many cards remaining in their deck as
            // the value of the card they just drew, the winner of the round is determined
            // by playing a new game of Recursive Combat (see below).
            if ( subGame ) {
                List<Queue<Integer>> subGameDecks = new ArrayList<>();
                for ( int i = 0; i < roundCards.size(); i++ ) {
                    int roundCard = roundCards.get( i );
                    Queue<Integer> playerDeck = playerDecks.get( i );
                    subGameDecks.add( new ArrayDeque<>(
                            new ArrayList<>( playerDeck ).subList( 0, roundCard ) ) );
                }

                roundWinnerIdx = playGameRecursive( subGameDecks );
            }

            // Give the round cards to the winner
            Queue<Integer> winnerDeck = playerDecks.get( roundWinnerIdx );
            winnerDeck.add( roundCards.get( roundWinnerIdx ) );
            roundCards.remove( roundWinnerIdx );
            winnerDeck.addAll( roundCards );

            // Check to see if we are still playing
            gameOn = false;
            for ( int i = 0; i < playerDecks.size(); i++ ) {
                if ( i != roundWinnerIdx ) {
                    Queue<Integer> playerDeck = playerDecks.get( i );
                    if ( ! playerDeck.isEmpty() ) {
                        gameOn = true;
                        break;
                    }
                }
            }
        }
        return roundWinnerIdx;
    }

    private long buildDeckValue( Queue<Integer> deck ) {
        List<Integer> winnerCards = new ArrayList<>( deck );
        long deckValue = 0;
        for ( int i = winnerCards.size() - 1, j = 1; i >= 0; i--, j++ ) {
            deckValue += ( winnerCards.get( i ) * j );
        }
        return deckValue;
    }

    private List<Queue<Integer>> parseDecks( List<String> lines ) {
        List<Queue<Integer>> playerDecks = new ArrayList<>();
        Queue<Integer> activeQueue = new ArrayDeque<>();
        for ( String line : lines ) {
            if ( line.contains( "Player" ) ) {
                activeQueue = new ArrayDeque<>();
                playerDecks.add( activeQueue );
            } else if ( ! line.trim().isEmpty() ) {
                activeQueue.add( Integer.parseInt( line ) );
            }
        }
        return playerDecks;
    }

}
