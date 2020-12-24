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
        List<Integer> winnerCards = new ArrayList<>( winnerDeck );

        long answer = 0;
        for ( int i = winnerCards.size() - 1, j = 1; i >= 0; i--, j++ ) {
            answer += ( winnerCards.get( i ) * j );
        }

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer);
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
