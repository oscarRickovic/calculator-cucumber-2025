package calculator;

import visitor.Visitor;

/**
 * This class represents mathematical constants such as PI, E, etc.
 * It implements the Expression interface so it can be used in calculator expressions.
 *
 * @see Expression
 */
public class MathConstant implements Expression {
    
    // Define a set of supported mathematical constants
    public static final String PI = "PI";      // π (3.14159...)
    public static final String E = "E";        // Euler's number (2.71828...)
    public static final String PHI = "PHI";    // Golden ratio (1.61803...)
    public static final String SQRT2 = "SQRT2"; // Square root of 2 (1.41421...)
    
    private final String constantName;
    private final Number value;
    
    /**
     * Constructor for a mathematical constant.
     *
     * @param constantName The name of the constant (PI, E, etc.)
     * @throws IllegalArgumentException If the constant name is not recognized
     */
    public MathConstant(String constantName) {
        this.constantName = constantName.toUpperCase();
        this.value = switch (this.constantName) {
            case PI -> Math.PI;
            case E -> Math.E;
            case PHI -> (1 + Math.sqrt(5)) / 2.0;
            case SQRT2 -> Math.sqrt(2);
            default -> throw new IllegalArgumentException("Unknown mathematical constant: " + constantName);
        };
    }
    
    /**
     * Get the value of this mathematical constant.
     *
     * @return The numerical value of the constant
     */
    public Number getValue() {
        return value;
    }
    
    /**
     * Get the name of this mathematical constant.
     *
     * @return The name of the constant (PI, E, etc.)
     */
    public String getConstantName() {
        return constantName;
    }
    
    /**
     * Accept method to implement the visitor design pattern.
     *
     * @param v The visitor object
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    /**
     * The depth of a mathematical constant expression is always 0
     *
     * @return The depth of a mathematical constant expression
     */
    @Override
    public int countDepth() {
        return 0;
    }

    /**
     * The number of operations contained in a mathematical constant expression is always 0
     *
     * @return The number of operations contained in a mathematical constant expression
     */
    @Override
    public int countOps() {
        return 0;
    }

    /**
     * The number of numbers contained in a mathematical constant expression is always 1
     *
     * @return The number of numbers contained in a mathematical constant expression
     */
    @Override
    public int countNbs() {
        return 1;
    }
    
    /**
     * Convert a mathematical constant into a String to allow it to be printed.
     *
     * @return The String that is the result of the conversion.
     */
    @Override
    public String toString() {
        return constantName;
    }
    
    /**
     * Two MathConstant expressions are equal if they represent the same constant
     *
     * @param o The object to compare to
     * @return A boolean representing the result of the equality test
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof MathConstant)) return false;
        
        MathConstant other = (MathConstant) o;
        return this.constantName.equals(other.constantName);
    }
    
    /**
     * The method hashCode needs to be overridden if the equals method is overridden
     *
     * @return The result of computing the hash.
     */
    @Override
    public int hashCode() {
        return constantName.hashCode();
    }
}