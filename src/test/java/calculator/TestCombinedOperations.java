package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class that combines the new operations: Sqrt and Modulo.
 */
public class TestCombinedOperations {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSqrtAndModuloTogether() throws Exception {
        // Test sqrt and modulo working together
        Expression e = StringToExpression.parseStringTExpression("sqrt(9) % 2");
        Object result = calc.eval(e);
        
        // sqrt(9) % 2 = 3 % 2 = 1
        assertTrue(result instanceof Number);
        assertEquals(1, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("sqrt(16 % 7)");
        result = calc.eval(e);
        
        // sqrt(16 % 7) = sqrt(2) ≈ 1.414
        assertTrue(result instanceof Number);
        assertEquals(Math.sqrt(2), ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexExpressions() throws Exception {
        // Test more complex expressions with both operations
        Expression e = StringToExpression.parseStringTExpression("(sqrt(16) + 5) % 7");
        Object result = calc.eval(e);
        
        // (sqrt(16) + 5) % 7 = (4 + 5) % 7 = 9 % 7 = 2
        assertTrue(result instanceof Number);
        assertEquals(2, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("sqrt(25 % 7) + 10");
        result = calc.eval(e);
        
        // sqrt(25 % 7) + 10 = sqrt(4) + 10 = 2 + 10 = 12
        assertTrue(result instanceof Number);
        assertEquals(12, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testWithOtherOperations() throws Exception {
        // Test with a mix of operations
        Expression e = StringToExpression.parseStringTExpression("2 * sqrt(9) + 10 % 3");
        Object result = calc.eval(e);
        
        // 2 * sqrt(9) + 10 % 3 = 2 * 3 + 10 % 3 = 6 + 1 = 7
        assertTrue(result instanceof Number);
        assertEquals(7, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("sqrt(16) * 2 + 5 % 2");
        result = calc.eval(e);
        
        // sqrt(16) * 2 + 5 % 2 = 4 * 2 + 5 % 2 = 8 + 1 = 9
        assertTrue(result instanceof Number);
        assertEquals(9, ((Number)result).doubleValue(), 0.0001);
        
        // Test with parentheses affecting precedence
        e = StringToExpression.parseStringTExpression("sqrt(16 % (1 + 6))");
        result = calc.eval(e);
        
        // sqrt(16 % (1 + 6)) = sqrt(16 % 7) = sqrt(2) ≈ 1.414
        assertTrue(result instanceof Number);
        assertEquals(Math.sqrt(2), ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testExpressionWithFunctions() throws Exception {
        // Test with trigonometric functions
        Expression e = StringToExpression.parseStringTExpression("sin(sqrt(9)) % 1");
        Object result = calc.eval(e);
        
        // sin(sqrt(9)) % 1 = sin(3) % 1 ≈ sin(3) % 1
        assertTrue(result instanceof Number);
        double expected = Math.sin(3) % 1;
        assertEquals(expected, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("sqrt(cos(0) % 1)");
        result = calc.eval(e);
        
        // sqrt(cos(0) % 1) = sqrt(1 % 1) = sqrt(0) = 0
        assertTrue(result instanceof Number);
        assertEquals(0, ((Number)result).doubleValue(), 0.0001);
    }
}