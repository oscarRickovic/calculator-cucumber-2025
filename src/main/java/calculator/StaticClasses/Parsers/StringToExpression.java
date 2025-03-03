package calculator.StaticClasses.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import calculator.Calculator;
import calculator.Divides;
import calculator.Expression;
import calculator.Minus;
import calculator.MyNumber;
import calculator.Plus;
import calculator.Times;
import calculator.StaticClasses.StaticHelpers;

public class StringToExpression {

    public static Expression parseStringTExpression(String stringExpression) {
        List<String> tokens = tokenize(stringExpression.replaceAll("\\s+", ""));
        List<String> postfix = infixToPostfix(tokens);
        return buildExpressionTree(postfix);
    }

    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numBuffer = new StringBuilder();

        for (char ch : expr.toCharArray()) {
            if (Character.isDigit(ch)) {
                numBuffer.append(ch);
            } else {
                if (numBuffer.length() > 0) {
                    tokens.add(numBuffer.toString());
                    numBuffer.setLength(0);
                }
                tokens.add(String.valueOf(ch));
            }
        }
        if (numBuffer.length() > 0) {
            tokens.add(numBuffer.toString());
        }
        return tokens;
    }

    private static List<String> infixToPostfix(List<String> tokens) {
        Map<String, Integer> precedence = Map.of("+", 1, "-", 1, "*", 2, "/", 2);
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.matches("\\d+")) {
                output.add(token);
            } else if ("+-*/".contains(token)) {
                while (!operators.isEmpty() && precedence.getOrDefault(operators.peek(), 0) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (StaticHelpers.openingBrackets.contains(token)) {
                operators.push(token);
            } else if (StaticHelpers.closingBrackets.contains(token)) {
                while (!operators.isEmpty() && !operators.peek().equals(StaticHelpers.matchingBrackets.get(token))) {
                    output.add(operators.pop());
                }
                if (!operators.isEmpty()) {
                    operators.pop();
                }
            }
        }
        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }
        return output;
    }

    private static Expression buildExpressionTree(List<String> postfix) {
        Stack<Expression> stack = new Stack<>();

        for (String token : postfix) {
            if (token.matches("\\d+")) {
                stack.push(new MyNumber(Integer.parseInt(token)));
            } else {
                Expression right = stack.pop();
                Expression left = stack.pop();
                try {
                    switch (token) {
                        case "+": stack.push(new Plus(null, left, right)); break;
                        case "-": stack.push(new Minus(null, left, right)); break;
                        case "*": stack.push(new Times(null, left, right)); break;
                        case "/": stack.push(new Divides(null, left, right)); break;
                    }
                } catch(Exception e) {/* do nothing */}
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        c.print(
            parseStringTExpression("{(4 * 3) + [2 * 5]} / 5)))")
        );

        c.print(
            parseStringTExpression("2 + 3 + 5 / 2")
        );
    }
}
