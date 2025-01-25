package com.harris;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AdventDay23 {

    public void executePart1() {
        System.out.println("ADVENT DAY 23 - PART 1...");
        //int[] labels = new int[]{ 3,8,9,1,2,5,4,6,7 };
        int[] labels = new int[]{ 8,7,1,3,6,9,4,5,2 };

        Instant start = Instant.now();

        CrabCups game = new CrabCups(labels, -1);
        Cup current = game.getCup(labels[0]);
        for ( int i = 0; i < 100; i++ ) {
            current = game.playRound(current);
        }

        StringBuilder sb = new StringBuilder();
        current = game.getCup(1).next;
        while (current.label != 1) {
            sb.append(current.label);
            current = current.next;
        }

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + sb );
    }

    public void executePart2() {
        System.out.println("ADVENT DAY 23 - PART 2...");
        //int[] labels = new int[]{ 3,8,9,1,2,5,4,6,7 };
        //int[] labels = new int[]{ 8,7,1,3,6,9,4,5,2 };

        Instant start = Instant.now();

        long answer = 0;

        Instant end = Instant.now();
        System.out.println("  [" + Duration.between(start, end) + "] Answer: " + answer );
    }
}

class Cup {
    int label;
    Cup next;
    Cup prev;
    public String toString() {
        return String.valueOf(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cup cup = (Cup) o;
        return label == cup.label;
    }

    @Override
    public int hashCode() {
        return label;
    }
}

class CrabCups {
    private final Map<Integer,Cup> cups = new HashMap<>();
    private int minLabel;
    private int maxLabel;

    CrabCups(int[] labels, int last) {
        Cup first = new Cup();
        first.label = labels[0];
        cups.put(first.label, first);
        Cup prev = first;
        minLabel = labels[0];
        maxLabel = labels[0];
        for (int i = 1; i < labels.length; i++) {
            Cup c = new Cup();
            c.label = labels[i];
            cups.put(c.label, c);
            c.prev = prev;
            prev.next = c;
            prev = c;
            if (labels[i] < minLabel) {
                minLabel = labels[i];
            }
            if (labels[i] > maxLabel) {
                maxLabel = labels[i];
            }
        }
        for (int label = maxLabel + 1; label <= last; label++) {
            Cup c = new Cup();
            c.label = label;
            cups.put(c.label, c);
            c.prev = prev;
            prev.next = c;
            prev = c;
            maxLabel = label;
        }
        prev.next = first;
        first.prev = prev;
    }

    Cup getCup(int label) {
        return cups.get(label);
    }

    Cup playRound(Cup start) {
        // select next 3 cups clockwise from start
        Cup[] selected = new Cup[3];
        selected[0] = start.next;
        selected[1] = selected[0].next;
        selected[2] = selected[1].next;

        // determine destination cup based on cup with a label
        // that is one minus the starting label, so long as it
        // is not a selected cup. wrap from min to max if necessary.
        int dest = start.label;
        do {
            dest--;
            if (dest < minLabel) {
                dest = maxLabel;
            }
        } while (dest == selected[0].label || dest == selected[1].label || dest == selected[2].label);
        Cup destCup = cups.get(dest);

        // move selected
        start.next = selected[2].next;
        selected[2].next = destCup.next;
        selected[2].next.prev = selected[2];
        destCup.next = selected[0];
        selected[0].prev = destCup;
        start.next.prev = start;

        // return the next cup after the start
        return start.next;
    }
}
