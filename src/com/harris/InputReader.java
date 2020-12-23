package com.harris;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InputReader {
    public List<Integer> readIntInput( String file ) {
        List<String> strings = readStringInput( file );
        List<Integer> ints = new ArrayList<>( strings.size() );
        for ( String s : strings ) {
            ints.add( Integer.parseInt( s ) );
        }
        return ints;
    }

    public List<Long> readLongInput( String file ) {
        List<String> strings = readStringInput( file );
        List<Long> longs = new ArrayList<>( strings.size() );
        for ( String s : strings ) {
            longs.add( Long.parseLong( s ) );
        }
        return longs;
    }

    public List<String> readStringInput( String file ) {
        List<String> input = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader( new FileReader( file ) );
            String line = br.readLine();
            while ( line != null ) {
                input.add( line );
                line = br.readLine();
            }
            br.close();
        } catch ( IOException ioe ) {
            throw new Error( "Error reading input", ioe );
        }

        return input;
    }
}
