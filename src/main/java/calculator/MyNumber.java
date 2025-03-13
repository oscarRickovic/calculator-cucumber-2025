package calculator;

import visitor.Visitor;

/**
 * MyNumber is a concrete class that represents arithmetic numbers,
 * which are a special kind of Expressions, just like operations are.
 *
 * @see Expression
 * @see Operation
 */
public class MyNumber implements Expression {
    private final Number value;
    
    /**
     * getter method to obtain the value contained in the object
     *
     * @return The number contained in the object
     */
    public Number getValue() { return value; }
    
    /**
     * Constructor method
     *
     * @param v The numeric value to be contained in the object
     */
    public /*constructor*/ MyNumber(Number v) {
        value = v;
    }
    
    /**
     * accept method to implement the visitor design pattern to traverse arithmetic expressions.
     * Each number will pass itself to the visitor object to get processed by the visitor.
     *
     * @param v The visitor object
     */
    public void accept(Visitor v) {
        v.visit(this);
    }
    
    /** The depth of a number expression is always 0
     *
     * @return The depth of a number expression
     */
    public int countDepth() {
        return 0;
    }
    
    /** The number of operations contained in a number expression is always 0
     *
     * @return The number of operations contained in a number expression
     */
    public int countOps() {
        return 0;
    }
    
    /** The number of numbers contained in a number expression is always 1
     *
     * @return The number of numbers contained in a number expression
     */
    public int countNbs() {
        return 1;
    }
    
    /**
     * Convert a number into a String to allow it to be printed.
     *
     * @return The String that is the result of the conversion.
     */
    @Override
    public String toString() {
        return value.toString();
    }
    
    /**
     * Two MyNumber expressions are equal if the values they contain are equal
     *
     * @param o The object to compare to
     * @return A boolean representing the result of the equality test
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof MyNumber)) return false;
        
        MyNumber other = (MyNumber) o;
        // Handle case where types might be different but values are equal (like Integer 5 and Double 5.0)
        if (this.value == null) return other.value == null;
        
        if (this.value.getClass() != other.value.getClass()) {
            // Compare as strings for different numeric types
            return this.value.toString().equals(other.value.toString());
        }
        
        // Safe to compare directly when types match
        return this.value.equals(other.value);
    }
    
    /**
     * The method hashCode needs to be overridden if the equals method is overridden
     *
     * @return The result of computing the hash.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}