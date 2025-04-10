package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the natural logarithm function operation "ln".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Ln extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("ln"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply natural logarithm to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Ln(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Ln(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "ln";
    neutral = 0; // No real neutral element for logarithm
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply natural logarithm to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Ln(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "ln";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Ln which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Ln is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Ln operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Ln which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Ln is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Ln operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Ln.
   * 
   * @param n The number to apply natural logarithm to
   * @return The natural logarithm of the number
   * @throws ArithmeticException if the number is less than or equal to zero
   */
  @Override
  public Number opUnary(Number n) {
    double value = n.doubleValue();
    if (value <= 0) {
      throw new ArithmeticException("Cannot compute the natural logarithm of a non-positive number");
    }
    return Math.log(value);
  }

  /**
   * The unary operation implementation for Ln with complex number.
   * Formula: ln(a+bi) = ln(|a+bi|) + i*arg(a+bi)
   * where |a+bi| = sqrt(a² + b²) and arg(a+bi) = atan2(b, a)
   * 
   * @param z The complex number to apply natural logarithm to
   * @return The natural logarithm of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    if (a == 0 && b == 0) {
      throw new ArithmeticException("Cannot compute the natural logarithm of zero");
    }
    
    // Compute the modulus (absolute value) of the complex number
    double modulus = Math.sqrt(a * a + b * b);
    
    // Compute the argument (phase) of the complex number
    double argument = Math.atan2(b, a);
    
    // ln(a+bi) = ln(|a+bi|) + i*arg(a+bi)
    double realPart = Math.log(modulus);
    double imagPart = argument;
    
    return new MyComplexNumber(realPart, imagPart);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the ln operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "ln(" + argStr + ")";
      case INFIX -> "ln(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")ln";
    };
  }
}