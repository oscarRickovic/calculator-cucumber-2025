package calculator;

/**
 * Interface to represent unary operations (operations that take a single parameter).
 * This interface is implemented by all unary operations like Sin, Cos, Ln, Exp, etc.
 */
public interface UnaryOperation {
    
    /**
     * Apply the unary operation to a real number.
     * 
     * @param n The number to apply the operation to
     * @return The result of the operation
     * @throws ArithmeticException If the operation cannot be performed on the given value
     */
    Number opUnary(Number n);
    
    /**
     * Apply the unary operation to a complex number.
     * 
     * @param z The complex number to apply the operation to
     * @return The result of the operation
     * @throws ArithmeticException If the operation cannot be performed on the given value
     */
    MyComplexNumber opUnaryComplex(MyComplexNumber z);
}