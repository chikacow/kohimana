package com.chikacow.kohimana.util.helper;

public class SmoothData {
    private SmoothData() {
        //pure note
        throw new IllegalStateException("Utility class");
    }
    public static String smooth(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }
}
