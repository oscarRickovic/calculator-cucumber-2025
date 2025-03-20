package calculator;

import visitor.Visitor;

/**
 * MyComplexNumber is a concrete class that represents complex numbers,
 * which are a special kind of Expressions, just like operations and regular numbers.
 * A complex number has a real part and an imaginary part.
 *
 * @see Expression
 * @see MyNumber
 * @see Operation
 */
public class MyComplexNumber implements Expression {
    private final Number realPart;
    private final Number imaginaryPart;
    
    /**
     * Constructor method for a complex number with real and imaginary parts
     *
     * @param real The real part of the complex number
     * @param imaginary The imaginary part of the complex number
     */
    public MyComplexNumber(Number real, Number imaginary) {
        this.realPart = real;
        this.imaginaryPart = imaginary;
    }
    
    /**
     * Getter method to obtain the real part of the complex number
     *
     * @return The real part of the complex number
     */
    public Number getRealPart() {
        return realPart;
    }
    
    /**
     * Getter method to obtain the imaginary part of the complex number
     *
     * @return The imaginary part of the complex number
     */
    public Number getImaginaryPart() {
        return imaginaryPart;
    }
    
    /**
     * Accept method to implement the visitor design pattern to traverse arithmetic expressions.
     * Each complex number will pass itself to the visitor object to get processed by the visitor.
     *
     * @param v The visitor object
     */
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
    
    /**
     * The depth of a complex number expression is always 0
     *
     * @return The depth of a complex number expression
     */
    @Override
    public int countDepth() {
        return 0;
    }
    
    /**
     * The number of operations contained in a complex number expression is always 0
     *
     * @return The number of operations contained in a complex number expression
     */
    @Override
    public int countOps() {
        return 0;
    }
    
    /**
     * The number of numbers contained in a complex number expression is always 1
     *
     * @return The number of numbers contained in a complex number expression
     */
    @Override
    public int countNbs() {
        return 1;
    }
    
    /**
     * Convert a complex number into a String to allow it to be printed.
     * Format: a+bi or a-bi (where a is the real part and b is the imaginary part)
     * Special cases for zero real or imaginary parts.
     *
     * @return The String that is the result of the conversion.
     */
    @Override
    public String toString() {
        double real = realPart.doubleValue();
        double imag = imaginaryPart.doubleValue();
        
        if (imag == 0) {
            return realPart.toString();
        } else if (real == 0) {
            return imag + "i";
        } else if (imag < 0) {
            return realPart.toString() + imag + "i";
        } else {
            return realPart.toString() + "+" + imag + "i";
        }
    }
    
    /**
     * Two MyComplexNumber expressions are equal if both their real and imaginary parts are equal
     *
     * @param o The object to compare to
     * @return A boolean representing the result of the equality test
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof MyComplexNumber)) return false;
        
        MyComplexNumber other = (MyComplexNumber) o;
        
        if (this.realPart == null || this.imaginaryPart == null) {
            return other.realPart == null && other.imaginaryPart == null;
        }
        
        // Compare real parts
        boolean realPartsEqual;
        if (this.realPart.getClass() != other.realPart.getClass()) {
            realPartsEqual = this.realPart.doubleValue() == other.realPart.doubleValue();
        } else {
            realPartsEqual = this.realPart.equals(other.realPart);
        }
        
        // Compare imaginary parts
        boolean imagPartsEqual;
        if (this.imaginaryPart.getClass() != other.imaginaryPart.getClass()) {
            imagPartsEqual = this.imaginaryPart.doubleValue() == other.imaginaryPart.doubleValue();
        } else {
            imagPartsEqual = this.imaginaryPart.equals(other.imaginaryPart);
        }
        
        return realPartsEqual && imagPartsEqual;
    }
    
    /**
     * The method hashCode needs to be overridden if the equals method is overridden
     *
     * @return The result of computing the hash.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (realPart != null ? realPart.hashCode() : 0);
        result = 31 * result + (imaginaryPart != null ? imaginaryPart.hashCode() : 0);
        return result;
    }
}