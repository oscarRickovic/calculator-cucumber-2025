package visitor;

import calculator.Expression;
import calculator.MyNumber;
import calculator.MyComplexNumber;
import calculator.Operation;
import calculator.Plus;
import calculator.Minus;
import calculator.Times;
import calculator.Divides;
import calculator.Sin;
import calculator.Cos;
import calculator.Ln;
import calculator.Exp;
import calculator.UnaryOperation;

import java.util.ArrayList;

/** Evaluation is a concrete visitor that serves to
 * compute and evaluate the results of arithmetic expressions.
 */
public class Evaluator extends Visitor {

    /**
     * Default constructor of the class. Does not initialise anything.
     */
    public Evaluator() {}

    /** The result of the evaluation will be stored in this private variable */
    private Object computedValue;

    /** getter method to obtain the result of the evaluation
     *
     * @return an Object containing the result of the evaluation (could be Number or MyComplexNumber)
     */
    public Object getResult() { 
        return computedValue; 
    }

    /** Use the visitor design pattern to visit a number.
     *
     * @param n The number being visited
     */
    public void visit(MyNumber n) {
        computedValue = n.getValue();
    }
    
    /** Use the visitor design pattern to visit a complex number.
     *
     * @param n The complex number being visited
     */
    public void visit(MyComplexNumber n) {
        computedValue = n;
    }

    /** Use the visitor design pattern to visit an operation
     *
     * @param o The operation being visited
     */
    public void visit(Operation o) {
        ArrayList<Object> evaluatedArgs = new ArrayList<>();
        boolean hasComplexOperand = false;
        
        // First loop to recursively evaluate each subexpression
        for (Expression a : o.args) {
            a.accept(this);
            evaluatedArgs.add(computedValue);
            if (computedValue instanceof MyComplexNumber) {
                hasComplexOperand = true;
            }
        }
        
        // Handle unary operations separately
        if (o instanceof UnaryOperation && evaluatedArgs.size() == 1) {
            handleUnaryOperation(o, evaluatedArgs.get(0), hasComplexOperand);
            return;
        }
        
        // If any operand is complex, convert all operands to complex
        if (hasComplexOperand) {
            // Convert any real numbers to complex numbers
            for (int i = 0; i < evaluatedArgs.size(); i++) {
                Object arg = evaluatedArgs.get(i);
                if (arg instanceof Number) {
                    evaluatedArgs.set(i, new MyComplexNumber((Number) arg, 0));
                }
            }
            
            // Compute with complex numbers
            MyComplexNumber result = (MyComplexNumber) evaluatedArgs.get(0);
            
            for (int i = 1; i < evaluatedArgs.size(); i++) {
                MyComplexNumber next = (MyComplexNumber) evaluatedArgs.get(i);
                
                if (o instanceof Plus) {
                    result = complexAdd(result, next);
                } else if (o instanceof Minus) {
                    result = complexSubtract(result, next);
                } else if (o instanceof Times) {
                    result = complexMultiply(result, next);
                } else if (o instanceof Divides) {
                    result = complexDivide(result, next);
                }
            }
            
            computedValue = result;
        } else {
            // All operands are real numbers, proceed with standard evaluation
            Number temp = (Number) evaluatedArgs.get(0);
            int max = evaluatedArgs.size();
            
            for (int counter = 1; counter < max; counter++) {
                try {
                    temp = o.op(temp, (Number) evaluatedArgs.get(counter));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            computedValue = temp;
        }
    }
    
    /**
     * Handle unary operation evaluation (sin, cos, ln, exp)
     * 
     * @param o The operation (must implement UnaryOperation)
     * @param arg The evaluated argument
     * @param isComplex Whether the argument is a complex number
     */
    private void handleUnaryOperation(Operation o, Object arg, boolean isComplex) {
        UnaryOperation unaryOp = (UnaryOperation) o;
        
        if (isComplex) {
            // Handle complex number
            MyComplexNumber z = (MyComplexNumber) arg;
            computedValue = unaryOp.opUnaryComplex(z);
        } else {
            // Handle real number
            Number n = (Number) arg;
            computedValue = unaryOp.opUnary(n);
        }
    }
    
    /**
     * Add two complex numbers.
     * (a+bi) + (c+di) = (a+c) + (b+d)i
     */
    private MyComplexNumber complexAdd(MyComplexNumber z1, MyComplexNumber z2) {
        double a = z1.getRealPart().doubleValue();
        double b = z1.getImaginaryPart().doubleValue();
        double c = z2.getRealPart().doubleValue();
        double d = z2.getImaginaryPart().doubleValue();
        
        return new MyComplexNumber(a + c, b + d);
    }
    
    /**
     * Subtract two complex numbers.
     * (a+bi) - (c+di) = (a-c) + (b-d)i
     */
    private MyComplexNumber complexSubtract(MyComplexNumber z1, MyComplexNumber z2) {
        double a = z1.getRealPart().doubleValue();
        double b = z1.getImaginaryPart().doubleValue();
        double c = z2.getRealPart().doubleValue();
        double d = z2.getImaginaryPart().doubleValue();
        
        return new MyComplexNumber(a - c, b - d);
    }
    
    /**
     * Multiply two complex numbers.
     * (a+bi) * (c+di) = (ac-bd) + (bc+ad)i
     */
    private MyComplexNumber complexMultiply(MyComplexNumber z1, MyComplexNumber z2) {
        double a = z1.getRealPart().doubleValue();
        double b = z1.getImaginaryPart().doubleValue();
        double c = z2.getRealPart().doubleValue();
        double d = z2.getImaginaryPart().doubleValue();
        
        return new MyComplexNumber(a * c - b * d, b * c + a * d);
    }
    
    /**
     * Divide two complex numbers.
     * (a+bi) / (c+di) = ((ac+bd)/(c²+d²)) + ((bc-ad)/(c²+d²))i
     */
    private MyComplexNumber complexDivide(MyComplexNumber z1, MyComplexNumber z2) {
        double a = z1.getRealPart().doubleValue();
        double b = z1.getImaginaryPart().doubleValue();
        double c = z2.getRealPart().doubleValue();
        double d = z2.getImaginaryPart().doubleValue();
        
        double denominator = c * c + d * d;
        
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        
        double realPart = (a * c + b * d) / denominator;
        double imagPart = (b * c - a * d) / denominator;
        
        return new MyComplexNumber(realPart, imagPart);
    }
}