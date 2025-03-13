package calculator.StaticClasses.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import calculator.Calculator;
import calculator.ComplexNumber;
import calculator.Divides;
import calculator.Expression;
import calculator.Minus;
import calculator.MyNumber;
import calculator.Plus;
import calculator.Times;
import calculator.StaticClasses.StaticHelpers;

public class StringToExpression {

    public static Expression parseStringTExpression(String stringExpression) throws Exception {
        List<String> tokens = tokenize(stringExpression.replaceAll("\\s+", ""));
        List<String> postfix = infixToPostfix(tokens);
        return buildExpressionTree(postfix);
    }

    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numBuffer = new StringBuilder();


        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (Character.isDigit(ch) || ch == '.' || ch == '-' || ch == 'i') {
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
        Map<String, Integer> precedence = Map.of("+", 1, "-", 1, "*", 2, "/", 2, "~", 3);
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();


        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?i?")) {
                System.out.println("✅ Operand detected: " + token);
                output.add(token);
            } else if ("+-*/~".contains(token)) {
                while (!operators.isEmpty() &&
                        precedence.getOrDefault(operators.peek(), 0) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else {
                System.out.println(" ERROR: Unknown token " + token);
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }

    private static Expression buildExpressionTree(List<String> postfix) throws Exception {
        Stack<Expression> stack = new Stack<>();
        Pattern complexPattern = Pattern.compile("-?\\d*(\\.\\d+)?i?");


        for (String token : postfix) {

            if (token.matches("-?\\d*(\\.\\d+)?i")) {
                double real = 0;
                double imaginary = 1.0;
                String numPart = token.replace("i", "");

                if (!numPart.isEmpty() && !numPart.equals("+") && !numPart.equals("-")) {
                    imaginary = Double.parseDouble(numPart);
                } else if (numPart.equals("-")) {
                    imaginary = -1.0;
                }

                stack.push(new MyNumber(new ComplexNumber(real, imaginary)));
            }
            else if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(new MyNumber(Double.parseDouble(token)));
            }
            else if ("+-*/".contains(token)) {
                if (stack.size() < 2) {
                    return null;
                }

                Expression right = stack.pop();
                Expression left = stack.pop();

                switch (token) {
                    case "+": stack.push(new Plus(null, left, right)); break;
                    case "-": stack.push(new Minus(null, left, right)); break;
                    case "*": stack.push(new Times(null, left, right)); break;
                    case "/": stack.push(new Divides(null, left, right)); break;
                    default:
                        return null;
                }
            }
            else {
                return null;
            }
        }

        if (stack.size() != 1) {
            return null;
        }

        return stack.pop();
    }

    public static void main(String[] args) {
        Calculator c = new Calculator();
        String[] testExpressions = {
                "3 + 2i",
                "4 - 3i",
                "(3 + 2i) + (5 + 4i)",
                "(3 + 2i) * (1 - i)",
                "(3 + 2i) / (1 + i)"
        };

        for (String expr : testExpressions) {
            try {
                Expression result = parseStringTExpression(expr);
                c.print(result);
            } catch (Exception e) {
                System.out.println("Error in the expression: " + e.getMessage());
            }
        }
    }
}