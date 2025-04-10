package visitor;

import calculator.MyNumber;
import calculator.MyComplexNumber;
import calculator.MathConstant;
import calculator.Operation;

/**
 * Visitor design pattern
 */
public abstract class Visitor {

    /**
     * The Visitor can traverse a number (a subtype of Expression)
     *
     * @param n The number being visited
     */
    public abstract void visit(MyNumber n);
    
    /**
     * The Visitor can traverse a complex number (a subtype of Expression)
     *
     * @param n The complex number being visited
     */
    public abstract void visit(MyComplexNumber n);
    
    /**
     * The Visitor can traverse a mathematical constant (a subtype of Expression)
     *
     * @param m The mathematical constant being visited
     */
    public abstract void visit(MathConstant m);

    /**
     * The Visitor can traverse an operation (a subtype of Expression)
     *
     * @param o The operation being visited
     */
    public abstract void visit(Operation o);
}