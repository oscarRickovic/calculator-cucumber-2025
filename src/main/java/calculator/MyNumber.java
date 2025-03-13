package calculator;

import visitor.Visitor;
import java.util.Objects;

/**
 * MyNumber is a concrete class that represents arithmetic numbers,
 * which are a special kind of Expressions, just like operations are.
 *
 * @see Expression
 * @see Operation
 */
public class MyNumber implements Expression {
    private final Number value;
    private final ComplexNumber complexValue;
    private final boolean isComplex;

    /**
     * getter method to obtain the value contained in the object
     *
     * @return The number contained in the object
     */
    public Number getValue() {
        return isComplex ? complexValue : value;
    }

    /**
     * Constructor method for real numbers
     *
     * @param v The numeric value to be contained in the object
     */
    public MyNumber(Number v) {
        this.value = v;
        this.complexValue = null;
        this.isComplex = false;
    }

    /**
     * Constructor method for complex numbers
     *
     * @param complexValue The complex number to be contained in the object
     */
    public MyNumber(ComplexNumber complexValue) {
        this.value = null;
        this.complexValue = complexValue;
        this.isComplex = true;
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
        if (isComplex) {
            return (complexValue != null) ? complexValue.toString() : "null";
        }
        return (value != null) ? value.toString() : "null";
    }

    /**
     * Two MyNumber expressions are equal if the values they contain are equal
     *
     * @param o The object to compare to
     * @return A boolean representing the result of the equality test
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyNumber myNumber = (MyNumber) o;
        return isComplex == myNumber.isComplex &&
                Objects.equals(value, myNumber.value) &&
                Objects.equals(complexValue, myNumber.complexValue);
    }

    /**
     * The method hashCode needs to be overridden if the equals method is overridden
     *
     * @return The result of computing the hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(value, complexValue, isComplex);
    }
}