package calculator.StaticClasses.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static Expression parseStringTExpression(String stringExpression) throws Exception {
        List<String> tokens = tokenize(stringExpression.replaceAll("\\s+", ""));
        List<String> postfix = infixToPostfix(tokens);
        return buildExpressionTree(postfix);
    }

    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numBuffer = new StringBuilder();
        boolean expectOperand = true; // True at start or after an operator or opening bracket
        int unaryMinusCount = 0;
    
        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
    
            if (Character.isDigit(ch)) {
                if (unaryMinusCount % 2 != 0) {
                    numBuffer.append('-'); // Apply unary minus only if odd count
                }
                numBuffer.append(ch);
                expectOperand = false;
                unaryMinusCount = 0; // Reset unary count after a number
            } else {
                if (numBuffer.length() > 0) {
                    tokens.add(numBuffer.toString());
                    numBuffer.setLength(0);
                }
    
                if (ch == '-') {
                    if (expectOperand) {
                        unaryMinusCount++; // Count unary minuses
                    } else {
                        tokens.add(String.valueOf(ch));
                        expectOperand = true;
                    }
                } else {
                    if (unaryMinusCount % 2 != 0) {
                        tokens.add("~"); // Convert odd number of unary minuses to one negation
                    }
                    unaryMinusCount = 0; // Reset count
                    tokens.add(String.valueOf(ch));
                    expectOperand = (ch != ')'); // Expect operand after operators or opening brackets
                }
            }
        }
    
        if (numBuffer.length() > 0) {
            tokens.add(numBuffer.toString());
        }
        if (unaryMinusCount % 2 != 0) {
            tokens.add("~");
        }
    
        return tokens;
    }
    
    
    private static List<String> infixToPostfix(List<String> tokens) {
        Map<String, Integer> precedence = Map.of("+", 1, "-", 1, "*", 2, "/", 2, "~", 3); // Unary negation has higher precedence
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
    
        for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                output.add(token);
            } else if ("+-*/~".contains(token)) {
                while (!operators.isEmpty() && 
                       precedence.getOrDefault(operators.peek(), 0) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else if (StaticHelpers.openingBrackets.contains(token)) {
                operators.push(token);
            } else if (StaticHelpers.closingBrackets.contains(token)) {
                while (!operators.isEmpty() && 
                       !operators.peek().equals(StaticHelpers.matchingBrackets.get(token))) {
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
    
    private static Expression buildExpressionTree(List<String> postfix) throws Exception {
        Stack<Expression> stack = new Stack<>();
    
        for (String token : postfix) {
            if (token.matches("-?\\d+")) {
                stack.push(new MyNumber(Integer.parseInt(token)));
            } else if (token.equals("~")) {
                // Handle unary negation
                if (stack.isEmpty()) return null;
                Expression operand = stack.pop();
                // Create a minus expression with 0 as the left operand
                stack.push(new Minus(null, new MyNumber(0), operand));
            } else {
                if (stack.size() < 2) return null;
                Expression right = stack.pop();
                Expression left = stack.pop();
                
                switch (token) {
                    case "+": stack.push(new Plus(null, left, right)); break;
                    case "-": stack.push(new Minus(null, left, right)); break;
                    case "*": stack.push(new Times(null, left, right)); break;
                    case "/": stack.push(new Divides(null, left, right)); break;
                }
                
            }
        }
        
        if (stack.size() != 1) return null;
        return stack.pop();
    }

    public static void main(String[] args){
        Calculator c = new Calculator();
        // Test with a variety of expressions including negative numbers
        String[] testExpressions = {
            "{(4 * 3) + [2 * 5]} / 5)))",
            "2 + 3 + 5 / 2",
            "-2 + 3",
            "3 - 2",
            "3-2",
            "3 -2",
            "-2+3",
            "5*-2",
            "-3*-4",
            "(-5+3)*2"
        };
        
        for (String expr : testExpressions) {
            System.out.println("Expression: " + expr);
            try{
                Expression result = parseStringTExpression(expr);
                c.print(result);
            } catch(Exception e ) {
                System.out.println("error in the expression");
            }
            
        }
    }
}