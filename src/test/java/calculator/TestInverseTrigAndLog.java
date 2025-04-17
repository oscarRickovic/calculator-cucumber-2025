package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for the inverse trigonometric functions (asin, acos, atan) and log.
 */
public class TestInverseTrigAndLog {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testASin() throws Exception {
        // Test basic arc sine operation
        Expression e = StringToExpression.parseStringTExpression("asin(0)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        // asin(0.5) = π/6 ≈ 0.5236
        e = StringToExpression.parseStringTExpression("asin(0.5)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(Math.PI/6, ((Number)result).doubleValue(), 0.0001);
        
        // asin(1) = π/2 ≈ 1.5708
        e = StringToExpression.parseStringTExpression("asin(1)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(Math.PI/2, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testACos() throws Exception {
        // Test basic arc cosine operation
        Expression e = StringToExpression.parseStringTExpression("acos(1)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        // acos(0.5) = π/3 ≈ 1.0472
        e = StringToExpression.parseStringTExpression("acos(0.5)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(Math.PI/3, ((Number)result).doubleValue(), 0.0001);
        
        // acos(0) = π/2 ≈ 1.5708
        e = StringToExpression.parseStringTExpression("acos(0)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(Math.PI/2, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testATan() throws Exception {
        // Test basic arc tangent operation
        Expression e = StringToExpression.parseStringTExpression("atan(0)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        // atan(1) = π/4 ≈ 0.7854
        e = StringToExpression.parseStringTExpression("atan(1)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(Math.PI/4, ((Number)result).doubleValue(), 0.0001);
        
        // atan(∞) approaches π/2, so let's test with a large number
        e = StringToExpression.parseStringTExpression("atan(10000)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(Math.PI/2, ((Number)result).doubleValue(), 0.001);
    }
    
    @Test
    void testLog() throws Exception {
        // Test basic logarithm operation
        Expression e = StringToExpression.parseStringTExpression("log(1)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        // log10(10) = 1
        e = StringToExpression.parseStringTExpression("log(10)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
        
        // log10(100) = 2
        e = StringToExpression.parseStringTExpression("log(100)");
        result = calc.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(2.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testASinOutOfRange() {
        // Test asin with values outside [-1, 1]
        assertThrows(Exception.class, () -> {
            Expression e = StringToExpression.parseStringTExpression("acos(-1.5)");
            calc.eval(e);
        });
    }
    
    @Test
    void testLogNonPositive() {
        // Test log with non-positive values
        assertThrows(Exception.class, () -> {
            Expression e = StringToExpression.parseStringTExpression("log(0)");
            calc.eval(e);
        });
        
        assertThrows(Exception.class, () -> {
            Expression e = StringToExpression.parseStringTExpression("log(-5)");
            calc.eval(e);
        });
    }
    
    @Test
    void testComplexASin() throws Exception {
        // Test asin with complex numbers
        Expression e = StringToExpression.parseStringTExpression("asin(2i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        
        // asin(2i) should have real part 0 and imaginary part approximately 1.317
        assertEquals(0.0, complex.getRealPart().doubleValue(), 0.001);
        assertTrue(complex.getImaginaryPart().doubleValue() > 0); // Should be positive
    }
    
    @Test
    void testComplexACos() throws Exception {
        // Test acos with complex numbers
        Expression e = StringToExpression.parseStringTExpression("acos(2i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
    }
    
    @Test
    void testInverseTrigWithExpressions() throws Exception {
        // Test inverse trig functions with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("asin(sin(0.5))");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.5, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("acos(cos(0.7))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.7, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("atan(tan(0.3))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.3, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testLogWithExpressions() throws Exception {
        // Test log with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("log(10^3)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(3.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("log(sqrt(100))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testInverseTrigIdentities() throws Exception {
        // Test some inverse trigonometric identities
        
        // sin(asin(x)) = x for x in [-1, 1]
        Expression e = StringToExpression.parseStringTExpression("sin(asin(0.5))");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.5, ((Number)result).doubleValue(), 0.0001);
        
        // cos(acos(x)) = x for x in [-1, 1]
        e = StringToExpression.parseStringTExpression("cos(acos(0.3))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.3, ((Number)result).doubleValue(), 0.0001);
        
        // tan(atan(x)) = x
        e = StringToExpression.parseStringTExpression("tan(atan(1.5))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.5, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testLogIdentities() throws Exception {
        // Test some logarithmic identities
        
        // 10^(log(x)) = x for x > 0
        Expression e = StringToExpression.parseStringTExpression("10^(log(5))");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(5.0, ((Number)result).doubleValue(), 0.0001);
        
        // log(a*b) = log(a) + log(b)
        e = StringToExpression.parseStringTExpression("log(2*5)");
        Object result1 = calc.eval(e);
        
        e = StringToExpression.parseStringTExpression("log(2) + log(5)");
        Object result2 = calc.eval(e);
        
        assertTrue(result1 instanceof Number);
        assertTrue(result2 instanceof Number);
        assertEquals(((Number)result1).doubleValue(), ((Number)result2).doubleValue(), 0.0001);
        
        // log(a/b) = log(a) - log(b)
        e = StringToExpression.parseStringTExpression("log(10/2)");
        result1 = calc.eval(e);
        
        e = StringToExpression.parseStringTExpression("log(10) - log(2)");
        result2 = calc.eval(e);
        
        assertTrue(result1 instanceof Number);
        assertTrue(result2 instanceof Number);
        assertEquals(((Number)result1).doubleValue(), ((Number)result2).doubleValue(), 0.0001);
    }
    
    @Test
    void testCombiningFunctions() throws Exception {
        // Test combining our new functions with other operations
        Expression e = StringToExpression.parseStringTExpression("2 * asin(0.5) + 1");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        // 2 * asin(0.5) + 1 = 2 * π/6 + 1 ≈ 2.0472
        assertEquals(2 * Math.PI/6 + 1, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("log(100) ^ 2");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        // log(100) ^ 2 = 2^2 = 4
        assertEquals(4.0, ((Number)result).doubleValue(), 0.0001);
    }
}
    