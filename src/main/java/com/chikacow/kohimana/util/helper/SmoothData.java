package com.chikacow.kohimana.util.helper;

public class SmoothData {
    public static String smooth(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }
}
