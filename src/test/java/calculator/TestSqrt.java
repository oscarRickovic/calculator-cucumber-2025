package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for the square root operation.
 */
public class TestSqrt {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSimpleSqrt() throws Exception {
        // Test basic square root operation
        Sqrt sqrt = new Sqrt(new MyNumber(4));
        
        Object result = calc.eval(sqrt);
        assertTrue(result instanceof Number);
        assertEquals(2.0, ((Number)result).doubleValue(), 0.0001);
        
        sqrt = new Sqrt(new MyNumber(9));
        result = calc.eval(sqrt);
        assertTrue(result instanceof Number);
        assertEquals(3.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testSqrtWithParser() throws Exception {
        // Test sqrt using string parsing
        Expression e = StringToExpression.parseStringTExpression("sqrt(4)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(2.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("sqrt(16)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(4.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testSqrtWithExpressions() throws Exception {
        // Test sqrt with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("sqrt(2+2)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(2.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("sqrt(3^2)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(3.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testSqrtWithDecimals() throws Exception {
        // Test sqrt with decimal numbers
        Expression e = StringToExpression.parseStringTExpression("sqrt(2.25)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.5, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testSqrtOfNegativeNumber() {
        // Test sqrt with negative number - should throw exception
        try {
            Sqrt sqrt = new Sqrt(new MyNumber(-4));
            Exception exception = assertThrows(ArithmeticException.class, () -> calc.eval(sqrt));
            assertTrue(exception.getMessage().contains("Cannot compute the square root of a negative number"));
        } catch (IllegalConstruction e) {
            fail("Construction failed");
        }
    }
    
    @Test
    void testSqrtOfZero() throws Exception {
        // Test sqrt of zero
        Expression e = StringToExpression.parseStringTExpression("sqrt(0)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testSqrtWithOperations() throws Exception {
        // Test combined operations with sqrt
        Expression e = StringToExpression.parseStringTExpression("1 + sqrt(9)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(4.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("sqrt(16) * 2");
        result = calc.eval(e);
    }
}