package calculator.StaticClasses.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import calculator.Calculator;
import calculator.Divides;
import calculator.Expression;
import calculator.Minus;
import calculator.MyNumber;
import calculator.MyComplexNumber;
import calculator.Plus;
import calculator.Times;
import calculator.StaticClasses.StaticHelpers;

/**
 * Utility class for parsing string expressions into Expression objects.
 * Handles infix expressions with support for decimal numbers, parentheses, complex numbers, and unary operations.
 */
public class StringToExpression {

    // Pattern to detect standalone complex numbers like 3+4i, -2-3i, 4i, etc.
    private static final Pattern COMPLEX_PATTERN = 
        Pattern.compile("^\\s*(-?\\d*\\.?\\d*)([-+]\\d*\\.?\\d*)?i\\s*$");

    /**
     * Parse a string representation of an arithmetic expression into an Expression object.
     * 
     * @param stringExpression The string to parse
     * @return An Expression object representing the parsed expression
     * @throws Exception If the expression is invalid or cannot be parsed
     */
    public static Expression parseStringTExpression(String stringExpression) throws Exception {
        if (stringExpression == null || stringExpression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        
        // Check if the entire expression is a single complex number
        if (isStandaloneComplexNumber(stringExpression.trim())) {
            return ComplexNumberParser.parse(stringExpression.trim());
        }
        
        try {
            List<String> tokens = tokenize(stringExpression);
            List<String> postfix = infixToPostfix(tokens);
            Expression result = buildExpressionTree(postfix);
            
            if (result == null) {
                throw new IllegalArgumentException("Failed to build expression tree from: " + stringExpression);
            }
            
            return result;
        } catch (Exception e) {
            throw new Exception("Error parsing expression '" + stringExpression + "': " + e.getMessage(), e);
        }
    }

    /**
     * Check if a string represents a standalone complex number (not part of a larger expression).
     */
    private static boolean isStandaloneComplexNumber(String str) {
        return COMPLEX_PATTERN.matcher(str).matches() || 
               str.trim().equals("i") || 
               str.trim().equals("-i");
    }

    /**
     * Tokenize a string expression into a list of tokens (numbers, complex numbers, operators, parentheses).
     */
    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numBuffer = new StringBuilder();
        boolean expectOperand = true; // True at start or after an operator or opening bracket
        int unaryMinusCount = 0; // Count consecutive unary minuses
        
        // Pre-process: insert spaces around operators and brackets for easier tokenization
        expr = expr.replaceAll("\\s+", " ")  // Normalize whitespace
                  .replaceAll("([\\+\\-\\*\\/\\(\\)\\[\\]\\{\\}])", " $1 ")  // Add spaces around operators and brackets
                  .replaceAll("\\s+", " ")  // Normalize whitespace again
                  .trim();  // Remove leading/trailing whitespace
        
        String[] parts = expr.split(" ");
        
        for (String part : parts) {
            if (part.isEmpty()) continue;
            
            // Check if it's a standalone complex number
            if (isStandaloneComplexNumber(part)) {
                if (unaryMinusCount > 0 && unaryMinusCount % 2 != 0) {
                    // Handle odd number of unary minuses before complex number
                    // We need to negate the complex number
                    MyComplexNumber complex = ComplexNumberParser.parse(part);
                    tokens.add(new MyComplexNumber(
                        negate(complex.getRealPart()),
                        negate(complex.getImaginaryPart())
                    ).toString());
                    unaryMinusCount = 0;
                } else {
                    tokens.add(part);
                    unaryMinusCount = 0;
                }
                expectOperand = false;
                continue;
            }
            
            // Check if it's a number
            if (part.matches("-?\\d+(\\.\\d+)?")) {
                if (expectOperand && unaryMinusCount > 0) {
                    // Apply unary minuses
                    if (unaryMinusCount % 2 != 0) {
                        // Odd number of minuses means negate
                        if (part.startsWith("-")) {
                            // If already negative, remove the negative sign
                            tokens.add(part.substring(1));
                        } else {
                            // If positive, make it negative
                            tokens.add("-" + part);
                        }
                    } else {
                        // Even number of minuses cancel out
                        tokens.add(part);
                    }
                    unaryMinusCount = 0;
                } else {
                    tokens.add(part);
                }
                expectOperand = false;
                continue;
            }
            
            // Check if it's an operator
            if ("+-*/".contains(part)) {
                if (part.equals("-") && expectOperand) {
                    // This is a unary minus
                    unaryMinusCount++;
                } else {
                    // Binary operator
                    tokens.add(part);
                    expectOperand = true;
                }
                continue;
            }
            
            // Check if it's a bracket
            if (StaticHelpers.openingBrackets.contains(part)) {
                tokens.add(part);
                expectOperand = true;
                continue;
            }
            
            if (StaticHelpers.closingBrackets.contains(part)) {
                tokens.add(part);
                expectOperand = false;
                continue;
            }
            
            // If we get here, it's an invalid token
            throw new IllegalArgumentException("Invalid token: " + part);
        }
        
        // Handle any trailing unary minuses
        if (unaryMinusCount > 0 && unaryMinusCount % 2 != 0) {
            tokens.add("u-");
        }
        
        return tokens;
    }
    
    /**
     * Negate a number.
     */
    private static Number negate(Number n) {
        if (n instanceof Double) {
            return -n.doubleValue();
        } else if (n instanceof Float) {
            return -n.floatValue();
        } else if (n instanceof Long) {
            return -n.longValue();
        } else if (n instanceof Integer) {
            return -n.intValue();
        } else if (n instanceof Short) {
            return (short) -n.shortValue();
        } else {
            return (byte) -n.byteValue();
        }
    }
    
    /**
     * Convert an infix expression to postfix (Reverse Polish Notation) using the Shunting-yard algorithm.
     */
    private static List<String> infixToPostfix(List<String> tokens) {
        // Define operator precedence
        Map<String, Integer> precedence = Map.of(
            "+", 1, 
            "-", 1, 
            "*", 2, 
            "/", 2, 
            "u-", 3  // Unary minus has higher precedence
        );
        
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        
        for (String token : tokens) {
            // Numbers and complex numbers go straight to output
            if (token.matches("-?\\d+(\\.\\d+)?") || isComplexNumber(token)) {
                output.add(token);
            } 
            // Handle unary minus and binary operators
            else if ("+-*/u-".contains(token)) {
                while (!operators.isEmpty() && 
                       !StaticHelpers.openingBrackets.contains(operators.peek()) &&
                       precedence.getOrDefault(operators.peek(), 0) >= precedence.get(token)) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } 
            // Handle opening brackets
            else if (StaticHelpers.openingBrackets.contains(token)) {
                operators.push(token);
            } 
            // Handle closing brackets
            else if (StaticHelpers.closingBrackets.contains(token)) {
                while (!operators.isEmpty() && 
                       !StaticHelpers.openingBrackets.contains(operators.peek())) {
                    output.add(operators.pop());
                }
                
                if (!operators.isEmpty() && 
                    StaticHelpers.openingBrackets.contains(operators.peek())) {
                    operators.pop(); // Discard the opening bracket
                } else {
                    throw new IllegalArgumentException("Mismatched brackets");
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        
        // Pop any remaining operators to the output
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (StaticHelpers.openingBrackets.contains(op)) {
                throw new IllegalArgumentException("Mismatched brackets");
            }
            output.add(op);
        }
        
        return output;
    }
    
    /**
     * Check if a token represents a complex number.
     */
    private static boolean isComplexNumber(String token) {
        return token.contains("i");
    }
    
    /**
     * Build an expression tree from a postfix expression.
     */
    private static Expression buildExpressionTree(List<String> postfix) throws Exception {
        if (postfix.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }
        
        Stack<Expression> stack = new Stack<>();
        
        for (String token : postfix) {
            // Handle complex numbers
            if (isComplexNumber(token)) {
                try {
                    MyComplexNumber complexNumber = ComplexNumberParser.parse(token);
                    stack.push(complexNumber);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid complex number format: " + token);
                }
            }
            // Handle regular numbers
            else if (token.matches("-?\\d+(\\.\\d+)?")) {
                // Parse as a double if it contains a decimal point, otherwise as an integer
                if (token.contains(".")) {
                    stack.push(new MyNumber(Double.parseDouble(token)));
                } else {
                    stack.push(new MyNumber(Integer.parseInt(token)));
                }
            } 
            // Handle unary minus
            else if (token.equals("u-")) {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression: missing operand for unary minus");
                }
                
                Expression operand = stack.pop();
                // Create a minus expression with 0 as the left operand
                stack.push(new Minus(null, new MyNumber(0), operand));
            } 
            // Handle binary operators
            else if ("+-*/".contains(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: insufficient operands for binary operator " + token);
                }
                
                Expression right = stack.pop();
                Expression left = stack.pop();
                
                switch (token) {
                    case "+": stack.push(new Plus(null, left, right)); break;
                    case "-": stack.push(new Minus(null, left, right)); break;
                    case "*": stack.push(new Times(null, left, right)); break;
                    case "/": stack.push(new Divides(null, left, right)); break;
                    default: throw new IllegalArgumentException("Unsupported operator: " + token);
                }
            } else {
                throw new IllegalArgumentException("Invalid token in postfix expression: " + token);
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: too many operands");
        }
        
        return stack.pop();
    }

    /**
     * Test method to validate parsing of various expressions, including complex numbers.
     */
    public static void main(String[] args) {
        Calculator c = new Calculator();
        
        // Test with a variety of expressions including complex numbers
        String[] testExpressions = {
            "3+4i",
            "3+4i + 2-i",
            "(3+4i) * (2-i)",
            "5 + 3i",
            "i + i",
            "2 * (3+4i)",
            "4 -- 2",
            "4 - - - 2",
            "2.5 + 3 + 5 / 2.2",
            "-2.5 + 3",
            "3 - 2.75",
            "3.33-2",
            "3 -2.1",
            "-2.0+3",
            "5*-2.5",
            "-3.14*-4",
            "(-5.5+3)*2",
            "50 - 3 * 4 + 10 - 2 * 5 + 8 * 2",
            "3.2 + 2.2"
        };
        
        for (String expr : testExpressions) {
            System.out.println("Expression: " + expr);
            try {
                Expression result = parseStringTExpression(expr);
                System.out.println("Result: " + c.eval(result));
            } catch (Exception e) {
                System.out.println("Error in the expression: " + e.getMessage());
            }
        }
    }
}