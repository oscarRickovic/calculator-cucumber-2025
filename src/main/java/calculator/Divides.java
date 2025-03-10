package calculator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** This class represents the arithmetic division operation "/".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Minus
 * @see Times
 * @see Plus
 */
public final class Divides extends Operation
{

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("/"));

  /**
   * Class constructor specifying a number of Expressions to divide.
   *
   * @param elist The list of Expressions to divide
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Divides(List<Expression>,Notation)
   */
  public /*constructor*/ Divides(List<Expression> elist) throws IllegalConstruction {
	  this(elist, null);
  }

    /**
     * Class constructor specifying a number of Expressions to divide,
     * as well as the notation used to represent the operation.
     *
     * @param elist The list of Expressions to divide
     * @param n The Notation to be used to represent the operation
     * @throws IllegalConstruction  If an empty list of expressions if passed as parameter
     * @see #Divides(List<Expression>)
     * @see Operation#Operation(List<Expression>,Notation)
     */
  public Divides(List<Expression> elist, Notation n) throws IllegalConstruction {
    super(elist,n);
    symbol = "/";
    neutral = 1;
  }

  public Divides(Notation n, Expression... elist) throws IllegalConstruction {
    super(n, elist);
    symbol = "/";
    neutral = 1;
  }



    /**
     * The actual computation of the (binary) arithmetic division of two integers
     * @param l The first integer
     * @param r The second integer that should divide the first
     * @return The integer that is the result of the division
     */
    public int op(int l, int r) {
        if (r == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return l / r;
    }

    public Number op(Number l, Number r) throws Exception {
      // Check for division by zero
      if (r.doubleValue() == 0.0) {
          throw new Exception("Can't divide by zero");
      }
      
      // Determine the highest precision type involved
      if (l instanceof Double || r instanceof Double) {
          return l.doubleValue() / r.doubleValue();
      } else if (l instanceof Float || r instanceof Float) {
          return l.floatValue() / r.floatValue();
      } else if (l instanceof Long || r instanceof Long) {
          return l.longValue() / r.longValue();
      } else if (l instanceof Integer || r instanceof Integer) {
          return l.intValue() / r.intValue();
      } else if (l instanceof Short || r instanceof Short) {
          return l.shortValue() / r.shortValue();
      } else {
          return l.byteValue() / r.byteValue();
      }
  }

}
