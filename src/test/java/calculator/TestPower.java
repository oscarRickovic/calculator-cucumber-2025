package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for the power operation.
 */
public class TestPower {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSimplePower() throws Exception {
        // Test basic power operation
        List<Expression> params = Arrays.asList(new MyNumber(2), new MyNumber(3));
        Power power = new Power(params);
        
        Object result = calc.eval(power);
        assertTrue(result instanceof Number);
        assertEquals(8.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerWithParser() throws Exception {
        // Test power using string parsing
        Expression e = StringToExpression.parseStringTExpression("2^3");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(8.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("3^2");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(9.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerWithExpressions() throws Exception {
        // Test power with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("2^(1+2)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(8.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("(1+1)^(1+2)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(8.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerWithDecimals() throws Exception {
        // Test power with decimal numbers
        Expression e = StringToExpression.parseStringTExpression("2.5^2");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(6.25, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("2^0.5");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(Math.sqrt(2), ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerWithNegativeNumbers() throws Exception {
        // Test power with negative numbers
        Expression e = StringToExpression.parseStringTExpression("(-2)^2");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(4.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("(-2)^3");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(-8.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testZeroPowers() throws Exception {
        // Test powers with zero
        Expression e = StringToExpression.parseStringTExpression("0^0");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("5^0");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("0^5");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerPrecedence() throws Exception {
        // Test power precedence
        Expression e = StringToExpression.parseStringTExpression("2^3*2");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(16.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("2*3^2");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(18.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerAssociativity() throws Exception {
        // Test power associativity (right-to-left)
        Expression e = StringToExpression.parseStringTExpression("2^3^2");
        Object result = calc.eval(e);
        
        // 2^3^2 should be evaluated as 2^(3^2) = 2^9 = 512, not (2^3)^2 = 8^2 = 64
        assertTrue(result instanceof Number);
        assertEquals(64.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testPowerWithComplexNumbers() throws Exception {
        // Test power with complex numbers
        Expression e = StringToExpression.parseStringTExpression("(3+4i)^2");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        // (3+4i)^2 = (3+4i)(3+4i) = 9 + 24i + 16i^2 = 9 + 24i - 16 = -7 + 24i
        assertEquals(-7.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(24.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testMixedWithFunctions() throws Exception {
        // Test power with functions
        Expression e = StringToExpression.parseStringTExpression("sin(2^2)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        // sin(2^2) = sin(4) ≈ -0.7568
        assertEquals(Math.sin(4), ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("2^sin(0)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        // 2^sin(0) = 2^0 = 1
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexExpressionWithPower() throws Exception {
        // Test a complex expression with power
        Expression e = StringToExpression.parseStringTExpression("2 + 3 * 4^2 - 5");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        // 2 + 3 * 4^2 - 5 = 2 + 3 * 16 - 5 = 2 + 48 - 5 = 45
        assertEquals(45.0, ((Number)result).doubleValue(), 0.0001);
    }
}