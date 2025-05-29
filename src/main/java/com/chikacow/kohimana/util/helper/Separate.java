package com.chikacow.kohimana.util.helper;

import com.chikacow.kohimana.exception.InvalidDataException;

public class Separate {
    private Separate() {
        //pure note
        throw new IllegalStateException("Utility class");
    }
    public static String getRidOfFirstWord(String input, String wordToBeRemoved) {
        if (input != null) {
            return input.substring(wordToBeRemoved.length() + 1);
        } else {
            throw new InvalidDataException("Wrong format");
        }


    }
}
