package com.chikacow.kohimana.util.helper;

public class Converter {
    private Converter() {
        //pure note
        throw new IllegalStateException("Utility class");
    }
    public static long evaluateExpression(String expression) {
        try {
            // Split the expression based on "*"
            String[] parts = expression.split("\\*");

            // Parse numbers
            int num1 = Integer.parseInt(parts[0].trim());
            int num2 = Integer.parseInt(parts[1].trim());

            // Return multiplication result
            return num1 * num2;
        } catch (Exception e) {
            throw new IllegalArgumentException("Wrong expression format, should looks like a computational text: " + expression);
        }
    }
}
