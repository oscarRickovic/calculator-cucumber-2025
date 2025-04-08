package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the square root function operation "sqrt".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Sqrt extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("sqrt"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply square root to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Sqrt(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Sqrt(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "sqrt";
    neutral = 0; // No real neutral element for square root
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply square root to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Sqrt(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "sqrt";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Sqrt which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Sqrt is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Sqrt operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Sqrt which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Sqrt is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Sqrt operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Sqrt.
   * 
   * @param n The number to apply square root to
   * @return The square root of the number
   * @throws ArithmeticException if the number is negative
   */
  @Override
  public Number opUnary(Number n) {
    double value = n.doubleValue();
    if (value < 0) {
      throw new ArithmeticException("Cannot compute the square root of a negative number");
    }
    return Math.sqrt(value);
  }

  /**
   * The unary operation implementation for Sqrt with complex number.
   * For a complex number z = a + bi:
   * sqrt(z) = sqrt(r) * (cos(θ/2) + i*sin(θ/2))
   * where r = |z| = sqrt(a² + b²) and θ = arg(z) = atan2(b, a)
   * 
   * @param z The complex number to apply square root to
   * @return The square root of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    // Calculate the modulus (absolute value) of the complex number
    double r = Math.sqrt(a * a + b * b);
    
    // Calculate the argument (phase) of the complex number
    double theta = Math.atan2(b, a);
    
    // Calculate the square root using the formula
    double sqrtR = Math.sqrt(r);
    double halfTheta = theta / 2;
    
    double realPart = sqrtR * Math.cos(halfTheta);
    double imagPart = sqrtR * Math.sin(halfTheta);
    
    return new MyComplexNumber(realPart, imagPart);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the sqrt operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "sqrt(" + argStr + ")";
      case INFIX -> "sqrt(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")sqrt";
    };
  }
}