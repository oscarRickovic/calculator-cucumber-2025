package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the tangent function operation "tan".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class Tan extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("tan"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply tangent to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Tan(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public Tan(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "tan";
    neutral = 0; // No real neutral element for tangent
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply tangent to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public Tan(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "tan";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Tan which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as Tan is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("Tan operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for Tan which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as Tan is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("Tan operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for Tan.
   * 
   * @param n The number to apply tangent to
   * @return The tangent of the number
   * @throws ArithmeticException if the input is a multiple of π/2 + nπ, where tangent is undefined
   */
  @Override
  public Number opUnary(Number n) {
    double value = n.doubleValue();
    
    // Check if the value is close to π/2 + nπ where tangent is undefined
    double modPi = value % Math.PI;
    if (Math.abs(modPi - Math.PI/2) < 1e-10 || Math.abs(modPi + Math.PI/2) < 1e-10) {
      throw new ArithmeticException("Tangent is undefined at x = π/2 + nπ");
    }
    
    return Math.tan(value);
  }

  /**
   * The unary operation implementation for Tan with complex number.
   * Formula: tan(a+bi) = [sin(2a) + i*sinh(2b)] / [cos(2a) + cosh(2b)]
   * 
   * @param z The complex number to apply tangent to
   * @return The tangent of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    // Using the formula: tan(a+bi) = sin(2a)/(cos(2a)+cosh(2b)) + i*sinh(2b)/(cos(2a)+cosh(2b))
    double sin2a = Math.sin(2 * a);
    double cos2a = Math.cos(2 * a);
    double sinh2b = Math.sinh(2 * b);
    double cosh2b = Math.cosh(2 * b);
    
    double denominator = cos2a + cosh2b;
    
    // Check for potential division by zero
    if (Math.abs(denominator) < 1e-10) {
      throw new ArithmeticException("Tangent is undefined for this complex number");
    }
    
    double realPart = sin2a / denominator;
    double imagPart = sinh2b / denominator;
    
    return new MyComplexNumber(realPart, imagPart);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the tan operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "tan(" + argStr + ")";
      case INFIX -> "tan(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")tan";
    };
  }
}