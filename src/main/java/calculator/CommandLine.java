package calculator;

import calculator.StaticClasses.StaticHelpers;
import calculator.StaticClasses.Parsers.StringToExpression;

public class CommandLine {

    public static void main(String[] args) {
        Calculator c = new Calculator();
        
        while (true) {
            System.out.print(">> ");

            String input = StaticHelpers.INPUT.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                break;
            }

            if (input.isEmpty()) continue; // Ignore empty lines

            try {
                System.out.println(c.eval(
                    StringToExpression.parseStringTExpression(input)
                ));
            } catch (Exception e) {
                System.out.println("Invalid expression: " + e.getMessage());
            }
        }
    }
}
