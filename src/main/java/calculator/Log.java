package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the base-10 logarithm function operation "log".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Log extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("log"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply logarithm to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Log(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Log(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "log";
    neutral = 0; // No real neutral element for logarithm
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply logarithm to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Log(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "log";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Log which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Log is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Log operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Log which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Log is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Log operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Log.
   * 
   * @param n The number to apply logarithm to
   * @return The base-10 logarithm of the number
   * @throws ArithmeticException if the number is less than or equal to zero
   */
  @Override
  public Number opUnary(Number n) {
    double value = n.doubleValue();
    if (value <= 0) {
      throw new ArithmeticException("Cannot compute the logarithm of a non-positive number");
    }
    return Math.log10(value);
  }

  /**
   * The unary operation implementation for Log with complex number.
   * Formula: log10(z) = ln(z) / ln(10)
   * Uses the natural logarithm of a complex number and divides by ln(10).
   * 
   * @param z The complex number to apply logarithm to
   * @return The base-10 logarithm of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    if (a == 0 && b == 0) {
      throw new ArithmeticException("Cannot compute the logarithm of zero");
    }
    
    // Compute natural logarithm of complex number first
    // ln(z) = ln|z| + i*arg(z) where |z| = sqrt(a² + b²) and arg(z) = atan2(b, a)
    double modulus = Math.sqrt(a * a + b * b);
    double argument = Math.atan2(b, a);
    
    // ln(z) = ln(|z|) + i*arg(z)
    double lnReal = Math.log(modulus);
    double lnImag = argument;
    
    // log10(z) = ln(z) / ln(10)
    double log10Real = lnReal / Math.log(10);
    double log10Imag = lnImag / Math.log(10);
    
    return new MyComplexNumber(log10Real, log10Imag);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the log operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "log(" + argStr + ")";
      case INFIX -> "log(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")log";
    };
  }
}