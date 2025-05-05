package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for solving linear equations with one or multiple variables.
 * Supports systems of linear equations using Gaussian elimination.
 */
public class LinearEquationSolver {

    // Debug flag for troubleshooting
    private static final boolean DEBUG = true;

    /**
     * Solves a single linear equation with one variable.
     * 
     * @param equation A string representing the linear equation (e.g., "2x + 3 = 7")
     * @return The solution as a formatted string (e.g., "x = 2")
     * @throws IllegalArgumentException If the equation is not linear or has no solution
     */
    public static String solveSingleVariable(String equation) {
        if (DEBUG) System.out.println("Solving equation: " + equation);
        
        // Remove all spaces
        equation = equation.replaceAll("\\s+", "");
        
        // Check if equation contains equals sign
        if (!equation.contains("=")) {
            throw new IllegalArgumentException("Equation must contain an equals sign (=)");
        }
        
        // Split into left and right sides
        String[] sides = equation.split("=");
        if (sides.length != 2) {
            throw new IllegalArgumentException("Equation must have exactly one equals sign");
        }
        
        String leftSide = sides[0];
        String rightSide = sides[1];
        
        if (DEBUG) {
            System.out.println("Left side: " + leftSide);
            System.out.println("Right side: " + rightSide);
        }
        
        // Find all variables in the equation
        Set<String> allVariables = new HashSet<>();
        allVariables.addAll(findAllVariables(leftSide));
        allVariables.addAll(findAllVariables(rightSide));
        
        if (allVariables.isEmpty()) {
            throw new IllegalArgumentException("No variables found in the equation");
        }
        
        if (allVariables.size() > 1) {
            throw new IllegalArgumentException("Multiple variables found: " + String.join(", ", allVariables) + 
                ". For multiple variables, use the system solver.");
        }
        
        // Get the single variable
        String variable = allVariables.iterator().next();
        
        if (DEBUG) System.out.println("Variable found: " + variable);
        
        // Move all terms to the left side: ax + b = 0
        // This is done by moving all terms from the right side to the left with their signs inverted
        double coefficientSum = 0;  // Coefficient of the variable (a)
        double constantSum = 0;     // Constant term (b)
        
        // Process left side
        coefficientSum += extractCoefficient(leftSide, variable);
        constantSum += extractConstant(leftSide);
        
        if (DEBUG) {
            System.out.println("After left side: coeff = " + coefficientSum + ", const = " + constantSum);
        }
        
        // Process right side (with inverted signs)
        coefficientSum -= extractCoefficient(rightSide, variable);
        constantSum -= extractConstant(rightSide);
        
        if (DEBUG) {
            System.out.println("After right side: coeff = " + coefficientSum + ", const = " + constantSum);
        }
        
        // If coefficient of variable is zero, check if it's a valid equation
        if (Math.abs(coefficientSum) < 1e-10) {
            if (Math.abs(constantSum) < 1e-10) {
                return "Identity: The equation is true for all values of " + variable;
            } else {
                return "No solution: The equation is inconsistent";
            }
        }
        
        // Solve for the variable: ax + b = 0 => x = -b/a
        double solution = -constantSum / coefficientSum;
        
        // Format the result with proper precision
        return variable + " = " + formatDouble(solution);
    }
    
    /**
     * Extract the coefficient of a variable from an expression.
     * 
     * @param expression The expression to analyze
     * @param variable The variable to find the coefficient for
     * @return The coefficient of the variable
     */
    private static double extractCoefficient(String expression, String variable) {
        double coefficient = 0.0;
        
        // Replace multi-character operators to avoid potential issues
        String processedExpr = preprocessExpression(expression);
        
        // Split the expression into terms by + and - operators, keeping the signs
        List<String> terms = splitIntoTerms(processedExpr);
        
        if (DEBUG) System.out.println("Terms for coefficient extraction: " + terms);
        
        for (String term : terms) {
            // Check if the term contains the variable
            if (term.contains(variable)) {
                // Extract the coefficient
                String coeffStr = term.replace(variable, "").trim();
                double coeff;
                
                // Handle different cases
                if (coeffStr.isEmpty() || coeffStr.equals("+")) {
                    coeff = 1.0;
                } else if (coeffStr.equals("-")) {
                    coeff = -1.0;
                } else {
                    try {
                        coeff = Double.parseDouble(coeffStr);
                    } catch (NumberFormatException e) {
                        if (DEBUG) System.out.println("Failed to parse coefficient: " + coeffStr);
                        throw new IllegalArgumentException("Invalid coefficient format in term: " + term);
                    }
                }
                
                coefficient += coeff;
                if (DEBUG) System.out.println("Found coefficient " + coeff + " for term " + term);
            }
        }
        
        return coefficient;
    }
    
    /**
     * Extract the constant term from an expression.
     * 
     * @param expression The expression to analyze
     * @return The constant term
     */
    private static double extractConstant(String expression) {
        double constant = 0.0;
        
        // Replace multi-character operators to avoid potential issues
        String processedExpr = preprocessExpression(expression);
        
        // Split the expression into terms by + and - operators, keeping the signs
        List<String> terms = splitIntoTerms(processedExpr);
        
        if (DEBUG) System.out.println("Terms for constant extraction: " + terms);
        
        for (String term : terms) {
            // Check if the term doesn't contain any variables (it's a constant)
            if (!containsVariable(term)) {
                try {
                    // Parse the constant value
                    if (term.isEmpty() || term.equals("+")) {
                        // Skip empty terms or lone plus signs
                        continue;
                    } else if (term.equals("-")) {
                        // Lone minus sign is not a valid constant
                        throw new IllegalArgumentException("Invalid constant format: lone minus sign");
                    } else {
                        double value = Double.parseDouble(term);
                        constant += value;
                        if (DEBUG) System.out.println("Found constant " + value + " for term " + term);
                    }
                } catch (NumberFormatException e) {
                    if (DEBUG) System.out.println("Failed to parse constant: " + term);
                    // If it's not a variable and not a number, it's an invalid term
                    throw new IllegalArgumentException("Invalid constant format: " + term);
                }
            }
        }
        
        return constant;
    }
    
    /**
     * Check if a string contains any alphabetic characters (variables).
     */
    private static boolean containsVariable(String str) {
        return str.matches(".*[a-zA-Z].*");
    }
    
    /**
     * Preprocess an expression to simplify parsing.
     */
    private static String preprocessExpression(String expression) {
        // Replace multiple operators with their equivalent
        expression = expression.replaceAll("\\+\\+", "+");
        expression = expression.replaceAll("--", "+");
        expression = expression.replaceAll("\\+-", "-");
        expression = expression.replaceAll("-\\+", "-");
        
        // Ensure the expression starts with a sign
        if (!expression.startsWith("+") && !expression.startsWith("-")) {
            expression = "+" + expression;
        }
        
        return expression;
    }
    
    /**
     * Split an expression into terms, preserving the signs.
     */
    private static List<String> splitIntoTerms(String expression) {
        List<String> terms = new ArrayList<>();
        
        // Use a regex pattern to find terms with their signs
        Pattern pattern = Pattern.compile("([+\\-][^+\\-]*)");
        Matcher matcher = pattern.matcher(expression);
        
        while (matcher.find()) {
            terms.add(matcher.group(1));
        }
        
        return terms;
    }
    
    /**
     * Solves a system of linear equations with multiple variables using Gaussian elimination.
     * 
     * @param equations An array of strings representing linear equations
     * @return A map of variable names to their solution values
     * @throws IllegalArgumentException If the system has no unique solution
     */
    public static Map<String, Double> solveSystem(String[] equations) {
        if (DEBUG) System.out.println("Solving system of " + equations.length + " equations");
        
        if (equations == null || equations.length == 0) {
            throw new IllegalArgumentException("No equations provided");
        }
        
        // Extract variables from all equations
        Set<String> variables = new HashSet<>();
        for (String equation : equations) {
            variables.addAll(findAllVariables(equation));
        }
        
        if (variables.isEmpty()) {
            throw new IllegalArgumentException("No variables found in the system");
        }
        
        // Convert variable names to an ordered array
        String[] variableArray = variables.toArray(new String[0]);
        Arrays.sort(variableArray); // Sort for deterministic ordering
        
        int numVariables = variableArray.length;
        int numEquations = equations.length;
        
        if (DEBUG) {
            System.out.println("Variables found: " + Arrays.toString(variableArray));
            System.out.println("Number of equations: " + numEquations);
        }
        
        // Create coefficient matrix [A|b] for Ax = b
        double[][] matrix = new double[numEquations][numVariables + 1];
        
        // Fill the matrix with coefficients and constants
        for (int i = 0; i < numEquations; i++) {
            String equation = equations[i];
            
            if (DEBUG) System.out.println("Processing equation: " + equation);
            
            // Split equation into left and right sides
            String[] sides = equation.replaceAll("\\s+", "").split("=");
            if (sides.length != 2) {
                throw new IllegalArgumentException("Equation must have exactly one equals sign: " + equation);
            }
            
            String leftSide = sides[0];
            String rightSide = sides[1];
            
            if (DEBUG) {
                System.out.println("  Left side: " + leftSide);
                System.out.println("  Right side: " + rightSide);
            }
            
            // Process left side (positive coefficients)
            for (int j = 0; j < numVariables; j++) {
                String variable = variableArray[j];
                matrix[i][j] += extractCoefficient(leftSide, variable);
            }
            matrix[i][numVariables] += extractConstant(leftSide);
            
            // Process right side (negative coefficients)
            for (int j = 0; j < numVariables; j++) {
                String variable = variableArray[j];
                matrix[i][j] -= extractCoefficient(rightSide, variable);
            }
            matrix[i][numVariables] -= extractConstant(rightSide);
        }
        
        if (DEBUG) {
            System.out.println("Coefficient matrix:");
            for (int i = 0; i < numEquations; i++) {
                System.out.println(Arrays.toString(matrix[i]));
            }
        }
        
        // Apply Gaussian elimination to get the solution
        reducedRowEchelonForm(matrix);
        
        if (DEBUG) {
            System.out.println("Row-echelon form:");
            for (int i = 0; i < numEquations; i++) {
                System.out.println(Arrays.toString(matrix[i]));
            }
        }
        
        // Check if the system has a unique solution
        checkSystemSolution(matrix, numVariables, numEquations);
        
        // Extract solutions from the row-echelon form
        Map<String, Double> solutions = new HashMap<>();
        for (int i = 0; i < Math.min(numVariables, numEquations); i++) {
            solutions.put(variableArray[i], matrix[i][numVariables]);
        }
        
        return solutions;
    }
    
    /**
     * Check if a system of equations has a unique solution.
     */
    private static void checkSystemSolution(double[][] matrix, int numVariables, int numEquations) {
        // Check for rows with all zeros in coefficient columns but non-zero constant (inconsistent)
        for (int i = 0; i < numEquations; i++) {
            boolean allZeroCoeffs = true;
            for (int j = 0; j < numVariables; j++) {
                if (Math.abs(matrix[i][j]) > 1e-10) {
                    allZeroCoeffs = false;
                    break;
                }
            }
            
            if (allZeroCoeffs && Math.abs(matrix[i][numVariables]) > 1e-10) {
                throw new IllegalArgumentException("The system is inconsistent (no solution)");
            }
        }
        
        // Check if the system is underdetermined (infinite solutions)
        if (numEquations < numVariables) {
            throw new IllegalArgumentException("The system is underdetermined (infinite solutions)");
        }
        
        // Check if the system is properly triangular (unique solution)
        for (int i = 0; i < Math.min(numVariables, numEquations); i++) {
            if (Math.abs(matrix[i][i] - 1.0) > 1e-10) {
                throw new IllegalArgumentException("The system does not have a unique solution");
            }
            
            for (int j = 0; j < i; j++) {
                if (Math.abs(matrix[i][j]) > 1e-10) {
                    throw new IllegalArgumentException("The system does not have a unique solution");
                }
            }
        }
    }
    
    /**
     * Formats a double value to a string, removing trailing zeros if it's a whole number.
     * 
     * @param value The double value to format
     * @return The formatted string
     */
    private static String formatDouble(double value) {
        // Handle very small values that should be zero
        if (Math.abs(value) < 1e-10) {
            return "0";
        }
        
        // Check if the value is an integer
        if (Math.abs(value - Math.round(value)) < 1e-10) {
            return Long.toString(Math.round(value));
        }
        
        // Format with reasonable precision for a calculator
        return String.format("%.6f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
    
    /**
     * Finds all variables in the expression.
     * 
     * @param expression The expression to search for variables
     * @return A set of all variables found
     */
    private static Set<String> findAllVariables(String expression) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*").matcher(expression);
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }
    
    /**
     * Applies Gaussian elimination to convert a matrix to reduced row-echelon form.
     * 
     * @param matrix The matrix to transform
     */
    private static void reducedRowEchelonForm(double[][] matrix) {
        int numRows = matrix.length;
        if (numRows == 0) return;
        
        int numCols = matrix[0].length;
        int lead = 0;
        
        for (int r = 0; r < numRows; r++) {
            if (lead >= numCols - 1) break;
            
            int i = r;
            while (i < numRows && Math.abs(matrix[i][lead]) < 1e-10) {
                i++;
            }
            
            if (i == numRows) {
                // No pivot in this column, move to next column
                lead++;
                r--; // Retry this row
                continue;
            }
            
            // Swap rows i and r
            double[] temp = matrix[i];
            matrix[i] = matrix[r];
            matrix[r] = temp;
            
            // Scale row r to make the leading coefficient 1
            double factor = matrix[r][lead];
            if (Math.abs(factor) > 1e-10) {
                for (int j = 0; j < numCols; j++) {
                    matrix[r][j] /= factor;
                }
            }
            
            // Subtract from other rows to make all other entries in the lead column 0
            for (i = 0; i < numRows; i++) {
                if (i != r) {
                    factor = matrix[i][lead];
                    for (int j = 0; j < numCols; j++) {
                        matrix[i][j] -= factor * matrix[r][j];
                    }
                }
            }
            
            lead++;
        }
        
        // Clean up very small values (numerical stability)
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (Math.abs(matrix[i][j]) < 1e-10) {
                    matrix[i][j] = 0.0;
                }
            }
        }
    }
}