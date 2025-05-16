package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** This class represents the power operation "^".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Plus
 * @see Minus
 * @see Times
 * @see Divides
 */
public final class Power extends Operation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("^"));

  /**
   * Class constructor specifying a number of Expressions for the power operation.
   *
   * @param elist The list of Expressions (base, exponent)
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Power(List<Expression>,Notation)
   */
  public /*constructor*/ Power(List<Expression> elist) throws IllegalConstruction {
  	this(elist, null);
  }

  /**
   * Class constructor specifying a number of Expressions for the power operation,
   * as well as the Notation used to represent the operation.
   *
   * @param elist The list of Expressions (base, exponent)
   * @param n The Notation to be used to represent the operation
   * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
   * @see #Power(List<Expression>)
   * @see Operation#Operation(List<Expression>,Notation)
   */
  public Power(List<Expression> elist, Notation n) throws IllegalConstruction {
  	super(elist,n);
  	symbol = "^";
  	neutral = 1; // Any number to the power of 0 is 1
  }

  /**
   * Constructor with variable number of expressions and notation.
   * 
   * @param n The notation to use
   * @param elist Variable number of expressions (base, exponent)
   * @throws IllegalConstruction If null is passed
   */
  public Power(Notation n, Expression... elist) throws IllegalConstruction {
  	super(n, elist);
  	symbol = "^";
  	neutral = 1;
  }

  /**
   * The actual computation of the (binary) power operation for integers.
   * @param l The base integer
   * @param r The exponent integer
   * @return The integer that is the result of the power operation
   */
  @Override
  public int op(int l, int r) {
  	if (r < 0) {
      // Negative exponents for integers should be handled differently
      // as they result in fractions, but we're returning integers here
      return (r == 0) ? 1 : 0; // Return 0 for negative exponents, as integer division truncates
    }
    
    // Calculate power using Math.pow and cast to int
    return (int)Math.pow(l, r);
  }

  /**
   * The power operation for Number type.
   * 
   * @param l The base number
   * @param r The exponent number
   * @return The number that is the result of the power operation
   */
  @Override
  public Number op(Number l, Number r) {
    // Calculate power operation with appropriate handling for different numeric types
    if (l instanceof Double || r instanceof Double || r.doubleValue() != r.intValue()) {
        return Math.pow(l.doubleValue(), r.doubleValue());
    } else if (l instanceof Float || r instanceof Float) {
        return (float)Math.pow(l.floatValue(), r.floatValue());
    } else if (l instanceof Long || r instanceof Long) {
        return (long)Math.pow(l.longValue(), r.intValue());
    } else {
        return (int)Math.pow(l.intValue(), r.intValue());
    }
  }
}