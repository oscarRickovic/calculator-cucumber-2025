package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for the new unary operations (sin, cos, ln, exp).
 */
public class TestUnaryOperations {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSinOperation() throws Exception {
        Sin sin = new Sin(new MyNumber(0));
        assertEquals(0.0, ((Number)calc.eval(sin)).doubleValue(), 0.0001);
        
        sin = new Sin(new MyNumber(Math.PI/2));
        assertEquals(1.0, ((Number)calc.eval(sin)).doubleValue(), 0.0001);
    }
    
    @Test
    void testCosOperation() throws Exception {
        Cos cos = new Cos(new MyNumber(0));
        assertEquals(1.0, ((Number)calc.eval(cos)).doubleValue(), 0.0001);
        
        cos = new Cos(new MyNumber(Math.PI));
        assertEquals(-1.0, ((Number)calc.eval(cos)).doubleValue(), 0.0001);
    }
    
    @Test
    void testLnOperation() throws Exception {
        Ln ln = new Ln(new MyNumber(1));
        assertEquals(0.0, ((Number)calc.eval(ln)).doubleValue(), 0.0001);
        
        ln = new Ln(new MyNumber(Math.E));
        assertEquals(1.0, ((Number)calc.eval(ln)).doubleValue(), 0.0001);
    }
    
    @Test
    void testExpOperation() throws Exception {
        Exp exp = new Exp(new MyNumber(0));
        assertEquals(1.0, ((Number)calc.eval(exp)).doubleValue(), 0.0001);
        
        exp = new Exp(new MyNumber(1));
        assertEquals(Math.E, ((Number)calc.eval(exp)).doubleValue(), 0.0001);
    }
    
    @Test
    void testLnOfNegativeNumber() {
        try {
            Ln ln = new Ln(new MyNumber(-1));
            Exception exception = assertThrows(ArithmeticException.class, () -> calc.eval(ln));
            assertTrue(exception.getMessage().contains("Cannot compute the natural logarithm of a non-positive number"));
        } catch (IllegalConstruction e) {
            fail("Construction failed");
        }
        
        
    }
    
    @Test
    void testComplexSin() throws Exception {
        // sin(i) = i*sinh(1) ≈ i*1.1752
        Sin sin = new Sin(new MyComplexNumber(0, 1));
        Object result = calc.eval(sin);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(0.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(1.1752, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexCos() throws Exception {
        // cos(i) = cosh(1) ≈ 1.5431
        Cos cos = new Cos(new MyComplexNumber(0, 1));
        Object result = calc.eval(cos);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(1.5431, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(0.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexExp() throws Exception {
        // exp(i*pi) = cos(pi) + i*sin(pi) = -1 + i*0
        Exp exp = new Exp(new MyComplexNumber(0, Math.PI));
        Object result = calc.eval(exp);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(-1.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(0.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexLn() throws Exception {
        // ln(i) = ln|i| + i*arg(i) = ln(1) + i*pi/2 = 0 + i*pi/2
        Ln ln = new Ln(new MyComplexNumber(0, 1));
        Object result = calc.eval(ln);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(0.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(Math.PI/2, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testStringParsingWithFunctions() throws Exception {
        // Test simple function calls
        Expression e = StringToExpression.parseStringTExpression("sin(0)");
        assertEquals(0.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("cos(0)");
        assertEquals(1.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("ln(1)");
        assertEquals(0.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("exp(0)");
        assertEquals(1.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
    }
    
    @Test
    void testNestedFunctions() throws Exception {
        // Test nested function calls
        Expression e = StringToExpression.parseStringTExpression("sin(cos(0))");
        // sin(cos(0)) = sin(1) ≈ 0.8415
        assertEquals(0.8415, ((Number)calc.eval(e)).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("exp(ln(10))");
        assertEquals(10.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
    }
    
    @Test
    void testFunctionsWithExpressions() throws Exception {
        // Test functions with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("sin(3.14159/2)");
        assertEquals(1.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("cos(2*3.14159)");
        assertEquals(1.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
    }
    
    @Test
    void testMixedExpressionsWithFunctions() throws Exception {
        // Test mixed expressions with functions
        Expression e = StringToExpression.parseStringTExpression("2 * sin(3.14159/6)");
        // 2 * sin(π/6) = 2 * 0.5 = 1.0
        assertEquals(1.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("1 + cos(0) + 2");
        // 1 + cos(0) + 2 = 1 + 1 + 2 = 4
        assertEquals(4.0, ((Number)calc.eval(e)).doubleValue(), 0.0001);
    }
    
    @Test
    void testFunctionsWithComplexNumbers() throws Exception {
        // Test functions with complex numbers
        Expression e = StringToExpression.parseStringTExpression("sin(3+4i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
    }
    
    @Test
    void testMixedExpressionsWithComplexAndFunctions() throws Exception {
        // Test expressions that mix complex numbers and functions
        Expression e = StringToExpression.parseStringTExpression("3+4i + sin(1)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        // 3+4i + sin(1) ≈ 3+4i + 0.8415 ≈ 3.8415+4i
        assertEquals(3.8415, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(4.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testInvalidOperations() {
        // Test invalid operations
        try {
            Sin sin = new Sin(null, new MyNumber(0));
            Exception exception = assertThrows(UnsupportedOperationException.class, 
            () -> sin.op(1, 2));
            assertTrue(exception.getMessage().contains("doesn't support binary evaluation"));
            
            exception = assertThrows(UnsupportedOperationException.class, 
                () -> sin.op(Integer.valueOf(1), Integer.valueOf(2)));
            assertTrue(exception.getMessage().contains("doesn't support binary evaluation"));
            } catch (IllegalConstruction e) {
                fail("Construction failed");
            }
    }
}