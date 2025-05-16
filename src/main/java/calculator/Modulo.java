package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** This class represents the modulo operation "%".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Plus
 * @see Minus
 * @see Times
 * @see Divides
 */
public final class Modulo extends Operation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("%"));

  /**
   * Class constructor specifying a number of Expressions for the modulo operation.
   *
   * @param elist The list of Expressions (dividend, divisor)
   * @throws IllegalConstruction If an empty list of expressions if passed as parameter
   * @see #Modulo(List<Expression>,Notation)
   */
  public /*constructor*/ Modulo(List<Expression> elist) throws IllegalConstruction {
    this(elist, null);
  }

  /**
   * Class constructor specifying a number of Expressions for the modulo operation,
   * as well as the Notation used to represent the operation.
   *
   * @param elist The list of Expressions (dividend, divisor)
   * @param n The Notation to be used to represent the operation
   * @throws IllegalConstruction If an empty list of expressions if passed as parameter
   * @see #Modulo(List<Expression>)
   * @see Operation#Operation(List<Expression>,Notation)
   */
  public Modulo(List<Expression> elist, Notation n) throws IllegalConstruction {
    super(elist, n);
    symbol = "%";
    neutral = 0; // No real neutral element for modulo
  }

  /**
   * Constructor with variable number of expressions and notation.
   * 
   * @param n The notation to use
   * @param elist Variable number of expressions (dividend, divisor)
   * @throws IllegalConstruction If null is passed
   */
  public Modulo(Notation n, Expression... elist) throws IllegalConstruction {
    super(n, elist);
    symbol = "%";
    neutral = 0;
  }

  /**
   * The actual computation of the (binary) modulo operation for integers.
   * @param l The dividend integer
   * @param r The divisor integer
   * @return The integer that is the result of the modulo operation
   * @throws ArithmeticException If the divisor is zero
   */
  @Override
  public int op(int l, int r) {
    if (r == 0) {
      throw new ArithmeticException("Modulo by zero is not allowed.");
    }
    return l % r;
  }

  /**
   * The modulo operation for Number type.
   * 
   * @param l The dividend number
   * @param r The divisor number
   * @return The number that is the result of the modulo operation
   * @throws ArithmeticException If the divisor is zero
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    // Check for modulo by zero
    if (r.doubleValue() == 0.0) {
      throw new ArithmeticException("Modulo by zero is not allowed.");
    }
    
    // Determine the highest precision type involved
    if (l instanceof Double || r instanceof Double) {
      return l.doubleValue() % r.doubleValue();
    } else if (l instanceof Float || r instanceof Float) {
      return l.floatValue() % r.floatValue();
    } else if (l instanceof Long || r instanceof Long) {
      return l.longValue() % r.longValue();
    } else if (l instanceof Integer || r instanceof Integer) {
      return l.intValue() % r.intValue();
    } else if (l instanceof Short || r instanceof Short) {
      return l.shortValue() % r.shortValue();
    } else {
      return l.byteValue() % r.byteValue();
    }
  }
}