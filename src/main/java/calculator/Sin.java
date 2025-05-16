package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the sine function operation "sin".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Sin extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("sin"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply sine to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Sin(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Sin(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "sin";
    neutral = 0; // No real neutral element for sine
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply sine to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Sin(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "sin";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Sin which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Sin is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Sin operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Sin which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Sin is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Sin operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Sin.
   * 
   * @param n The number to apply sine to
   * @return The sine of the number
   */
  @Override
  public Number opUnary(Number n) {
    return Math.sin(n.doubleValue());
  }

  /**
   * The unary operation implementation for Sin with complex number.
   * Formula: sin(a+bi) = sin(a)cosh(b) + i*cos(a)sinh(b)
   * 
   * @param z The complex number to apply sine to
   * @return The sine of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    double realPart = Math.sin(a) * Math.cosh(b);
    double imagPart = Math.cos(a) * Math.sinh(b);
    
    return new MyComplexNumber(realPart, imagPart);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the sin operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "sin(" + argStr + ")";
      case INFIX -> "sin(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")sin";
    };
  }
} 