package calculator.StaticClasses.Parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import calculator.Calculator;
import calculator.Divides;
import calculator.Expression;
import calculator.MathConstant;
import calculator.Minus;
import calculator.Modulo;
import calculator.MyNumber;
import calculator.MyComplexNumber;
import calculator.Plus;
import calculator.Times;
import calculator.Power;
import calculator.Sin;
import calculator.Cos;
import calculator.Tan;
import calculator.ASin;
import calculator.ACos;
import calculator.ATan;
import calculator.Ln;
import calculator.Log;
import calculator.Exp;
import calculator.Sqrt;
import calculator.StaticClasses.StaticHelpers;

/**
 * Utility class for parsing string expressions into Expression objects.
 * Handles infix expressions with support for decimal numbers, parentheses, complex numbers, functions and constants.
 */
public class StringToExpression {

    // Pattern to detect standalone complex numbers like 3+4i, -2-3i, 4i, etc.
    // Fixed: Limited repetition to avoid potential ReDoS
    private static final Pattern COMPLEX_PATTERN = 
        Pattern.compile("^\\s*(-?\\d{0,20}\\.?\\d{0,20})([-+]\\d{0,20}\\.?\\d{0,20})?i\\s*$");
        
    // Valid function names (all lowercase)
    private static final List<String> FUNCTION_NAMES = List.of("sin", "cos", "tan", "asin", "acos", "atan", "ln", "log", "exp", "sqrt");
    
    // Valid mathematical constants
    private static final Set<String> CONSTANTS = Set.of("PI", "E", "PHI", "SQRT2");

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
        
        // Check maximum input length to prevent ReDoS attacks
        if (stringExpression.length() > 5000) {
            throw new IllegalArgumentException("Expression is too long (max 5000 characters)");
        }
        
        // Check if the entire expression is a single complex number
        if (isStandaloneComplexNumber(stringExpression.trim())) {
            return ComplexNumberParser.parse(stringExpression.trim());
        }
        
        try {
            // Lowercase function names for case-insensitive parsing
            String normalizedExpression = normalizeFunctionNames(stringExpression);
            
            List<String> tokens = tokenize(normalizedExpression);
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
     * Normalize function names to lowercase for case-insensitive parsing.
     */
    private static String normalizeFunctionNames(String expr) {
        String result = expr;
        for (String funcName : FUNCTION_NAMES) {
            // Case-insensitive replacement with fixed pattern
            // Fixed: using a safer pattern with limited repetition
            Pattern pattern = Pattern.compile("(?i)" + Pattern.quote(funcName));
            result = pattern.matcher(result).replaceAll(funcName);
        }
        return result;
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
     * Tokenize a string expression into a list of tokens (constants, numbers, complex numbers, operators, parentheses).
     */
    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numBuffer = new StringBuilder();
        boolean expectOperand = true; // True at start or after an operator or opening bracket
        int unaryMinusCount = 0; // Count consecutive unary minuses
        
        // Pre-process: handle function calls
        for (String funcName : FUNCTION_NAMES) {
            // Fixed: use a safer way to replace function calls
            expr = expr.replace(funcName + "(", funcName + " ( ");
        }
        
        // Pre-process: insert spaces around operators and brackets for easier tokenization
        // Fixed: using a simpler approach with string replacements instead of regex
        String[] operators = {"+", "-", "*", "/", "^", "%", "(", ")", "[", "]", "{", "}", ","};
        for (String op : operators) {
            expr = expr.replace(op, " " + op + " ");
        }
        
        expr = expr.replaceAll("\\s+", " ").trim();  // Normalize whitespace
        
        String[] parts = expr.split(" ");
        
        for (String part : parts) {
            if (part.isEmpty()) continue;
            
            // Check if it's a function name
            if (FUNCTION_NAMES.contains(part.toLowerCase())) {
                if (unaryMinusCount > 0) {
                    throw new IllegalArgumentException("Cannot apply unary minus directly to a function name");
                }
                tokens.add(part.toLowerCase());
                expectOperand = false;  // Function name is followed by opening parenthesis, not an operand
                continue;
            }
            
            // Check if it's a mathematical constant
            if (CONSTANTS.contains(part.toUpperCase())) {
                if (unaryMinusCount > 0 && unaryMinusCount % 2 != 0) {
                    // Handle odd number of unary minuses before constant
                    tokens.add("u-");
                    tokens.add(part.toUpperCase());
                } else {
                    tokens.add(part.toUpperCase());
                }
                unaryMinusCount = 0;
                expectOperand = false;
                continue;
            }
            
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
            
            // Check if it's a number - Fixed: using a safer pattern with limited repetition
            if (part.matches("-?\\d{1,20}(\\.\\d{1,20})?")) {
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
            
            // Check if it's a comma (for function arguments)
            if (part.equals(",")) {
                tokens.add(part);
                expectOperand = true;
                continue;
            }
            
            // Check if it's an operator
            if ("+-*/^%".contains(part)) {
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
     * Modified to handle function calls.
     */
    private static List<String> infixToPostfix(List<String> tokens) {
        // Define operator and function precedence
        Map<String, Integer> precedence = Map.ofEntries(
        Map.entry("+", 1),
        Map.entry("-", 1),
        Map.entry("*", 2),
        Map.entry("/", 2),
        Map.entry("%", 2),    // Modulo
        Map.entry("^", 3),    // Power
        Map.entry("u-", 4),   // Unary minus
        Map.entry("sin", 5),
        Map.entry("cos", 5),
        Map.entry("tan", 5),  // Tangent
        Map.entry("asin", 5),
        Map.entry("acos", 5),
        Map.entry("atan", 5),
        Map.entry("ln", 5),
        Map.entry("log", 5),
        Map.entry("exp", 5),
        Map.entry("sqrt", 5)
    );

        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            
            // Numbers, complex numbers, and constants go straight to output
            if (token.matches("-?\\d{1,20}(\\.\\d{1,20})?") || isComplexNumber(token) || CONSTANTS.contains(token)) {
                output.add(token);
            } 
            // Handle function names
            else if (FUNCTION_NAMES.contains(token)) {
                operators.push(token);
            }
            // Handle commas (function argument separators)
            else if (token.equals(",")) {
                // Pop operators until we find a left parenthesis
                while (!operators.isEmpty() && !StaticHelpers.openingBrackets.contains(operators.peek())) {
                    output.add(operators.pop());
                }
                // If we didn't find a left parenthesis, there's a mismatched parenthesis
                if (operators.isEmpty() || !StaticHelpers.openingBrackets.contains(operators.peek())) {
                    throw new IllegalArgumentException("Mismatched parentheses or invalid comma placement");
                }
            }
            // Handle unary minus and binary operators
            else if ("+-*/^%u-".contains(token)) {
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
                    
                    // If the token at the top of the operator stack is a function token, pop it onto the output queue
                    if (!operators.isEmpty() && FUNCTION_NAMES.contains(operators.peek())) {
                        output.add(operators.pop());
                    }
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
     * This method ensures function names containing 'i' aren't mistaken for complex numbers.
     */
    private static boolean isComplexNumber(String token) {
        // First, check if it's a function name - these are not complex numbers
        if (FUNCTION_NAMES.contains(token.toLowerCase())) {
            return false;
        }
        
        // Check for standalone i or -i
        if (token.equals("i") || token.equals("-i")) {
            return true;
        }
        
        // Check for patterns like 3+4i, 2i, etc.
        // Fixed: using safer patterns with limited repetition
        return token.matches(".*\\d{1,20}i$") || 
               token.matches(".*\\d{1,20}\\.\\d{1,20}i$") ||
               token.matches(".*[-+]\\d{1,20}i$") || 
               token.matches(".*[-+]\\d{1,20}\\.\\d{1,20}i$") ||
               COMPLEX_PATTERN.matcher(token).matches();
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
            // Handle mathematical constants
            else if (CONSTANTS.contains(token)) {
                stack.push(new MathConstant(token));
            }
            // Handle regular numbers
            else if (token.matches("-?\\d{1,20}(\\.\\d{1,20})?")) {
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
            // Handle functions (unary operations)
            else if (FUNCTION_NAMES.contains(token)) {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression: missing operand for " + token);
                }
                
                Expression operand = stack.pop();
                
                switch (token) {
                    case "sin": stack.push(new Sin(null, operand)); break;
                    case "cos": stack.push(new Cos(null, operand)); break;
                    case "tan": stack.push(new Tan(null, operand)); break;
                    case "asin": stack.push(new ASin(null, operand)); break;
                    case "acos": stack.push(new ACos(null, operand)); break;
                    case "atan": stack.push(new ATan(null, operand)); break;
                    case "ln": stack.push(new Ln(null, operand)); break;
                    case "log": stack.push(new Log(null, operand)); break;
                    case "exp": stack.push(new Exp(null, operand)); break;
                    case "sqrt": stack.push(new Sqrt(null, operand)); break;
                    default: throw new IllegalArgumentException("Unsupported function: " + token);
                }
            }
            // Handle binary operators
            else if ("+-*/%^".contains(token)) {
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
                    case "%": stack.push(new Modulo(null, left, right)); break;
                    case "^": stack.push(new Power(null, left, right)); break;
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
}