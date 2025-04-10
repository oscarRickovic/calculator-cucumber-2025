package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the exponential function operation "exp".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Exp extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("exp"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply exponential to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Exp(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Exp(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "exp";
    neutral = 0; // No real neutral element for exponential
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply exponential to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Exp(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "exp";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Exp which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Exp is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Exp operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Exp which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Exp is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Exp operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Exp.
   * 
   * @param n The number to apply exponential to
   * @return The exponential of the number (e^n)
   */
  @Override
  public Number opUnary(Number n) {
    return Math.exp(n.doubleValue());
  }

  /**
   * The unary operation implementation for Exp with complex number.
   * Formula: exp(a+bi) = exp(a) * (cos(b) + i*sin(b))
   * 
   * @param z The complex number to apply exponential to
   * @return The exponential of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    // e^(a+bi) = e^a * (cos(b) + i*sin(b))
    double expA = Math.exp(a);
    double realPart = expA * Math.cos(b);
    double imagPart = expA * Math.sin(b);
    
    return new MyComplexNumber(realPart, imagPart);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the exp operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "exp(" + argStr + ")";
      case INFIX -> "exp(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")exp";
    };
  }
}