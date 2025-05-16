package calculator;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Class for evaluating mathematical expressions containing variables.
 */
public class FunctionEvaluator {

    private String originalExpression;
    private Calculator calculator;
    
    /**
     * Create a new function evaluator for a given expression
     * 
     * @param expression the mathematical expression with variables (e.g., "x^2 + 2*x + 1")
     */
    public FunctionEvaluator(String expression) {
        this.originalExpression = expression;
        this.calculator = new Calculator();
    }
    
    /**
     * Evaluate the expression for a specific value of x
     * 
     * @param x the value to substitute for x
     * @return the result of evaluating the expression
     * @throws Exception if the expression cannot be evaluated
     */
    public double evaluate(double x) throws Exception {
        // Replace the variable 'x' with the actual value
        String substitutedExpression = substituteVariable(originalExpression, x);
        
        // Parse and evaluate the expression
        Expression parsedExpression = StringToExpression.parseStringTExpression(substitutedExpression);
        Object result = calculator.eval(parsedExpression);
        
        // Convert result to double
        if (result instanceof Number) {
            return ((Number) result).doubleValue();
        } else if (result instanceof MyComplexNumber) {
            MyComplexNumber complex = (MyComplexNumber) result;
            if (complex.getImaginaryPart().doubleValue() == 0) {
                return complex.getRealPart().doubleValue();
            } else {
                throw new IllegalArgumentException("Function returned a complex number with non-zero imaginary part");
            }
        }
        
        throw new IllegalArgumentException("Function evaluation did not return a numeric result");
    }
    
    /**
     * Substitute a value for the variable x in the expression
     * 
     * @param expression the expression containing x
     * @param value the value to substitute for x
     * @return the expression with x replaced by the value
     */
    private String substituteVariable(String expression, double value) {
        // Handle negative values correctly by adding parentheses
        String replacement = value < 0 ? "(" + value + ")" : String.valueOf(value);
        
        // Replace standalone 'x' variables, ensuring we don't replace parts of other identifiers
        // This regex looks for 'x' that is:
        // - not preceded by a letter, digit, or underscore (negative lookbehind)
        // - not followed by a letter, digit, or underscore (negative lookahead)
        return expression.replaceAll("(?<![a-zA-Z0-9_])x(?![a-zA-Z0-9_])", replacement);
    }
}