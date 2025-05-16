package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the cosine function operation "cos".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Cos extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("cos"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply cosine to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Cos(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Cos(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "cos";
    neutral = 0; // No real neutral element for cosine
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply cosine to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Cos(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "cos";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Cos which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Cos is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Cos operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Cos which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Cos is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Cos operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Cos.
   * 
   * @param n The number to apply cosine to
   * @return The cosine of the number
   */
  @Override
  public Number opUnary(Number n) {
    return Math.cos(n.doubleValue());
  }

  /**
   * The unary operation implementation for Cos with complex number.
   * Formula: cos(a+bi) = cos(a)cosh(b) - i*sin(a)sinh(b)
   * 
   * @param z The complex number to apply cosine to
   * @return The cosine of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    double realPart = Math.cos(a) * Math.cosh(b);
    double imagPart = -Math.sin(a) * Math.sinh(b);
    
    return new MyComplexNumber(realPart, imagPart);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the cos operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "cos(" + argStr + ")";
      case INFIX -> "cos(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")cos";
    };
  }
}