package visitor;

import calculator.ComplexNumber;
import calculator.Expression;
import calculator.MyNumber;
import calculator.Operation;

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
    private Number computedValue;

    /** getter method to obtain the result of the evaluation
     *
     * @return an Integer object containing the result of the evaluation
     */
    public Number getResult() { return computedValue; }

    /** Use the visitor design pattern to visit a number.
     *
     * @param n The number being visited
     */
    public void visit(MyNumber n) {
        computedValue = n.getValue();
    }

    /** Use the visitor design pattern to visit an operation
     *
     * @param o The operation being visited
     */
    public void visit(Operation o) {
        ArrayList<Number> evaluatedArgs = new ArrayList<>();
        //first loop to recursively evaluate each subexpression
        for(Expression a:o.args) {
            a.accept(this);
            evaluatedArgs.add(computedValue);
        }
        //second loop to accumulate all the evaluated subresults
        Number temp = evaluatedArgs.get(0);
        int max = evaluatedArgs.size();
        for(int counter=1; counter<max; counter++) {
            try {
                if (temp instanceof ComplexNumber || evaluatedArgs.get(counter) instanceof ComplexNumber) {
                    ComplexNumber c1 = (temp instanceof ComplexNumber) ? (ComplexNumber) temp : new ComplexNumber(temp.doubleValue(), 0);
                    ComplexNumber c2 = (evaluatedArgs.get(counter) instanceof ComplexNumber) ? (ComplexNumber) evaluatedArgs.get(counter) : new ComplexNumber(evaluatedArgs.get(counter).doubleValue(), 0);
                    temp = o.op(c1, c2);
                } else {
                    temp = o.op(temp, evaluatedArgs.get(counter));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // store the accumulated result
        computedValue = temp;
    }

}
