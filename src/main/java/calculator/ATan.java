package calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the arc tangent function operation "atan".
 * The class extends the abstract superclass Operation.
 * @see Operation
 */
public final class ATan extends Operation implements UnaryOperation {

  protected static final List<String> SYMBOLES = new ArrayList<>(List.of("atan"));

  /**
   * Class constructor specifying a single Expression argument.
   *
   * @param expr The Expression to apply arc tangent to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public ATan(Expression expr) throws IllegalConstruction {
    this(Collections.singletonList(expr));
  }

  /**
   * Class constructor specifying an Expression list.
   * This constructor exists for compatibility with the Operation hierarchy.
   *
   * @param elist The list of Expressions (should contain exactly one element)
   * @throws IllegalConstruction If null or empty list is passed as parameter
   */
  public ATan(List<Expression> elist) throws IllegalConstruction {
    super(elist, null);
    symbol = "atan";
    neutral = 0; // No real neutral element for arc tangent
  }

  /**
   * Class constructor specifying a Notation and an Expression.
   *
   * @param n The Notation to be used
   * @param expr The Expression to apply arc tangent to
   * @throws IllegalConstruction If a null expression is passed as parameter
   */
  public ATan(Notation n, Expression expr) throws IllegalConstruction {
    super(n, expr);
    symbol = "atan";
    neutral = 0;
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for ATan which is unary.
   * 
   * @param l The first integer
   * @param r The second integer
   * @return This method throws an exception as ATan is not a binary operation
   */
  @Override
  public int op(int l, int r) {
    throw new UnsupportedOperationException("ATan operation doesn't support binary evaluation");
  }

  /**
   * The binary operation implementation required by Operation abstract class.
   * This method is not intended to be used for ATan which is unary.
   * 
   * @param l The first number
   * @param r The second number
   * @return This method throws an exception as ATan is not a binary operation
   */
  @Override
  public Number op(Number l, Number r) throws Exception {
    throw new UnsupportedOperationException("ATan operation doesn't support binary evaluation");
  }

  /**
   * The unary operation implementation for ATan.
   * 
   * @param n The number to apply arc tangent to
   * @return The arc tangent of the number
   */
  @Override
  public Number opUnary(Number n) {
    return Math.atan(n.doubleValue());
  }

  /**
   * The unary operation implementation for ATan with complex number.
   * Formula: atan(z) = 1/2 * i * (ln(1 - iz) - ln(1 + iz))
   * 
   * @param z The complex number to apply arc tangent to
   * @return The arc tangent of the complex number
   */
  @Override
  public MyComplexNumber opUnaryComplex(MyComplexNumber z) {
    double a = z.getRealPart().doubleValue();
    double b = z.getImaginaryPart().doubleValue();
    
    // For purely real inputs, return real result
    if (b == 0) {
      return new MyComplexNumber(Math.atan(a), 0);
    }
    
    // Implementation of atan(z) = 1/2 * i * (ln(1 - iz) - ln(1 + iz))
    
    // Step 1: Calculate iz
    double izReal = -b;
    double izImag = a;
    
    // Step 2: Calculate 1 - iz
    double minusReal = 1 - izReal;
    double minusImag = -izImag;
    
    // Step 3: Calculate 1 + iz
    double plusReal = 1 + izReal;
    double plusImag = izImag;
    
    // Step 4: Calculate ln(1 - iz)
    double lnMinusModulus = Math.log(Math.sqrt(minusReal * minusReal + minusImag * minusImag));
    double lnMinusArgument = Math.atan2(minusImag, minusReal);
    
    double lnMinusReal = lnMinusModulus;
    double lnMinusImag = lnMinusArgument;
    
    // Step 5: Calculate ln(1 + iz)
    double lnPlusModulus = Math.log(Math.sqrt(plusReal * plusReal + plusImag * plusImag));
    double lnPlusArgument = Math.atan2(plusImag, plusReal);
    
    double lnPlusReal = lnPlusModulus;
    double lnPlusImag = lnPlusArgument;
    
    // Step 6: Calculate ln(1 - iz) - ln(1 + iz)
    double diffReal = lnMinusReal - lnPlusReal;
    double diffImag = lnMinusImag - lnPlusImag;
    
    // Step 7: Calculate 1/2 * i * diff
    // Multiplying by i rotates by 90 degrees, which transforms (a, b) to (-b, a)
    double resultReal = -0.5 * diffImag;
    double resultImag = 0.5 * diffReal;
    
    return new MyComplexNumber(resultReal, resultImag);
  }

  /**
   * Override the toString method to provide appropriate representation for unary function.
   * 
   * @param n The notation to be used
   * @return The String representation of the atan operation
   */
  @Override
  public String toString(Notation n) {
    if (args.size() != 1) {
      return super.toString(n); // Fall back to standard behavior if not exactly one arg
    }
    
    String argStr = args.get(0).toString();
    
    return switch (n) {
      case PREFIX -> "atan(" + argStr + ")";
      case INFIX -> "atan(" + argStr + ")";  // Functions are typically represented the same in all notations
      case POSTFIX -> "(" + argStr + ")atan";
    };
  }
}