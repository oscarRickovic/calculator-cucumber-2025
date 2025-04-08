package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for the tangent operation.
 */
public class TestTan {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSimpleTan() throws Exception {
        // Test basic tangent operation
        Tan tan = new Tan(new MyNumber(0));
        
        Object result = calc.eval(tan);
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        // tan(π/4) = 1
        tan = new Tan(new MyNumber(Math.PI/4));
        result = calc.eval(tan);
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testTanWithParser() throws Exception {
        // Test tan using string parsing
        Expression e = StringToExpression.parseStringTExpression("tan(0)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        // tan(π/4) = 1
        e = StringToExpression.parseStringTExpression("tan(3.14159265359/4)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.01); // Using larger tolerance due to PI approximation
    }
    
    @Test
    void testTanWithExpressions() throws Exception {
        // Test tan with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("tan(2-2)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("tan(sin(0))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testTanWithDecimals() throws Exception {
        // Test tan with decimal numbers
        Expression e = StringToExpression.parseStringTExpression("tan(0.5)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(Math.tan(0.5), ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testTanUndefined() {
        // Test tan at points where it's undefined (π/2 + nπ)
        try {
            Tan tan = new Tan(new MyNumber(Math.PI/2));
            Exception exception = assertThrows(ArithmeticException.class, () -> calc.eval(tan));
            assertTrue(exception.getMessage().contains("Tangent is undefined"));
        } catch (IllegalConstruction e) {
            fail("Construction failed");
        }
    }
    
    @Test
    void testComplexTan() throws Exception {
        // Test tangent with complex numbers
        Tan tan = new Tan(new MyComplexNumber(0, 1));
        Object result = calc.eval(tan);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        
        // tan(i) = i*tanh(1) ≈ i*0.7616
        assertEquals(0.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(Math.tanh(1), complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testTanWithOtherFunctions() throws Exception {
        // Test tan in combination with other functions
        Expression e = StringToExpression.parseStringTExpression("sin(tan(0))");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("tan(cos(0))");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(Math.tan(1), ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testTanWithOperations() throws Exception {
        // Test combined operations with tan
        Expression e = StringToExpression.parseStringTExpression("1 + tan(0)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("tan(3.14159265359/4) * 2");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(2.0, ((Number)result).doubleValue(), 0.01); // Using larger tolerance due to PI approximation
    }
    
    @Test
    void testTanWithModuloAndSqrt() throws Exception {
        // Test tan with our newly added operations (modulo and sqrt)
        Expression e = StringToExpression.parseStringTExpression("tan(sqrt(0))");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("tan(5 % 2)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        // tan(5 % 2) = tan(1) ≈ 1.557
        assertEquals(Math.tan(1), ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testNotation() throws Exception {
        // Test different notations
        MyNumber num = new MyNumber(0);
        Tan tan = new Tan(Notation.PREFIX, num);
        assertEquals("tan(0)", tan.toString());
        
        tan = new Tan(Notation.INFIX, num);
        assertEquals("tan(0)", tan.toString());
        
        tan = new Tan(Notation.POSTFIX, num);
        assertEquals("(0)tan", tan.toString());
    }
    
    @Test
    void testInvalidOperation() throws Exception {
        // Test that binary operation methods throw exceptions
        Tan tan = new Tan(new MyNumber(0));
        
        Exception exception = assertThrows(UnsupportedOperationException.class, 
            () -> tan.op(1, 2));
        assertTrue(exception.getMessage().contains("Tan operation doesn't support binary evaluation"));
        
        exception = assertThrows(UnsupportedOperationException.class, 
            () -> tan.op(Integer.valueOf(1), Integer.valueOf(2)));
        assertTrue(exception.getMessage().contains("Tan operation doesn't support binary evaluation"));
    }
}