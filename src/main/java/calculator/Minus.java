package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** This class represents the arithmetic operation "-".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Plus
 * @see Times
 * @see Divides
 */
public final class Minus extends Operation
 {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("-"));

  /**
   * Class constructor specifying a number of Expressions to subtract.
   *
   * @param elist The list of Expressions to subtract
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Minus(List<Expression>,Notation)
   */
  public /*constructor*/ Minus(List<Expression> elist) throws IllegalConstruction {
  	this(elist, null);
  }

  /**
   * Class constructor specifying a number of Expressions to subtract,
   * as well as the Notation used to represent the operation.
   *
   * @param elist The list of Expressions to subtract
   * @param n The Notation to be used to represent the operation
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Minus(List<Expression>)
   * @see Operation#Operation(List<Expression>,Notation)
   */
  public Minus(List<Expression> elist, Notation n) throws IllegalConstruction {
  	super(elist,n);
  	symbol = "-";
  	neutral = 0;
  }

  public Minus(Notation n, Expression... elist) throws IllegalConstruction {
  	super(n, elist);
  	symbol = "-";
  	neutral = 0;
  }

    /**
     * The actual computation of the (binary) arithmetic subtraction of two integers
     * @param l The first integer
     * @param r The second integer that should be subtracted from the first
     * @return The integer that is the result of the subtraction
     */
  public int op(int l, int r) {
  	return (l-r);
  }

  public Number op(Number l, Number r) {
    if (l instanceof Double || r instanceof Double) {
        return l.doubleValue() - r.doubleValue();
    } else if (l instanceof Float || r instanceof Float) {
        return l.floatValue() - r.floatValue();
    } else if (l instanceof Long || r instanceof Long) {
        return l.longValue() - r.longValue();
    } else if (l instanceof Integer || r instanceof Integer) {
        return l.intValue() - r.intValue();
    } else if (l instanceof Short || r instanceof Short) {
        return l.shortValue() - r.shortValue();
    } else {
        return l.byteValue() - r.byteValue();
    }
  }
}
