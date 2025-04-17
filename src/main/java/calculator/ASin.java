package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the arc sine function operation "asin".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class ASin extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("asin"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply arc sine to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public ASin(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public ASin(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "asin";
    neutral = 0; // No real neutral element for arc sine
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply arc sine to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public ASin(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "asin";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for ASin which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as ASin is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("ASin operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for ASin which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as ASin is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("ASin operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for ASin.
   * 
   * @param n The number to apply arc sine to
   * @return The arc sine of the number
   * @throws ArithmeticException if the number is outside the range [-1, 1]
   */
  @Override
  public Number opUnary(Number n) {
    double value = n.doubleValue();
    if (value < -1 || value > 1) {
      throw new ArithmeticException("Arc sine is only defined for values in the range [-1, 1]");
    }
    return Math.asin(value);
  }

  /**
   * The unary operation implementation for ASin with complex number.
   * Formula: asin(z) = -i * ln(iz + sqrt(1 - z^2))
   * 
   * @param z The complex number to apply arc sine to
   * @return The arc sine of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    // For purely real inputs within [-1, 1], return real result
    if (b == 0 && a >= -1 && a <= 1) {
      return new MyComplexNumber(Math.asin(a), 0);
    }
    
    // Implementation of asin(z) = -i * ln(iz + sqrt(1 - z^2))
    
    // Step 1: Calculate 1 - z^2
    double real1 = 1 - (a * a - b * b);
    double imag1 = -2 * a * b;
    
    // Step 2: Calculate sqrt(1 - z^2)
    double modulus = Math.sqrt(real1 * real1 + imag1 * imag1);
    double argument = Math.atan2(imag1, real1);
    
    double sqrtReal = Math.sqrt(modulus) * Math.cos(argument / 2);
    double sqrtImag = Math.sqrt(modulus) * Math.sin(argument / 2);
    
    // Step 3: Calculate iz
    double izReal = -b;
    double izImag = a;
    
    // Step 4: Calculate iz + sqrt(1 - z^2)
    double sumReal = izReal + sqrtReal;
    double sumImag = izImag + sqrtImag;
    
    // Step 5: Calculate ln(iz + sqrt(1 - z^2))
    double lnModulus = Math.log(Math.sqrt(sumReal * sumReal + sumImag * sumImag));
    double lnArgument = Math.atan2(sumImag, sumReal);
    
    double lnReal = lnModulus;
    double lnImag = lnArgument;
    
    // Step 6: Calculate -i * ln(...)
    // Multiplying by -i is equivalent to rotating by -90 degrees
    // which transforms (a, b) to (b, -a)
    return new MyComplexNumber(lnImag, -lnReal);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the asin operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "asin(" + argStr + ")";
      case INFIX -> "asin(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")asin";
    };
  }
}