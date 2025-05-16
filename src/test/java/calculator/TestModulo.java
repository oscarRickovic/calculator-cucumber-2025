package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for the modulo operation.
 */
public class TestModulo {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSimpleModulo() throws Exception {
        // Test basic modulo operation
        List<Expression> params = Arrays.asList(new MyNumber(5), new MyNumber(2));
        Modulo modulo = new Modulo(params);
        
        Object result = calc.eval(modulo);
        assertTrue(result instanceof Number);
        assertEquals(1, ((Number)result).intValue());
        
        params = Arrays.asList(new MyNumber(10), new MyNumber(3));
        modulo = new Modulo(params);
        result = calc.eval(modulo);
        
        assertTrue(result instanceof Number);
        assertEquals(1, ((Number)result).intValue());
    }
    
    @Test
    void testModuloWithParser() throws Exception {
        // Test modulo using string parsing
        Expression e = StringToExpression.parseStringTExpression("5 % 2");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("10 % 3");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1, ((Number)result).intValue());
    }
    
    @Test
    void testModuloWithExpressions() throws Exception {
        // Test modulo with expressions as arguments
        Expression e = StringToExpression.parseStringTExpression("(2+3) % 3");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(2, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("10 % (1+1)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0, ((Number)result).intValue());
    }
    
    @Test
    void testModuloWithDecimals() throws Exception {
        // Test modulo with decimal numbers
        Expression e = StringToExpression.parseStringTExpression("5.5 % 2.5");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.5, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("10.5 % 3.5");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testModuloWithZeroDividend() throws Exception {
        // Test modulo with zero dividend
        Expression e = StringToExpression.parseStringTExpression("0 % 5");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0, ((Number)result).intValue());
    }
    
    @Test
    void testModuloWithNegativeNumbers() throws Exception {
        // Test modulo with negative numbers
        // In Java, the sign of the result follows the dividend
        Expression e = StringToExpression.parseStringTExpression("-7 % 3");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(-1, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("7 % -3");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("-7 % -3");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(-1, ((Number)result).intValue());
    }
    
    @Test
    void testModuloWithOperations() throws Exception {
        // Test combined operations with modulo
        Expression e = StringToExpression.parseStringTExpression("1 + (10 % 3)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(2, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("(15 % 4) * 2");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(6, ((Number)result).intValue());
    }
    
    @Test
    void testPrecedence() throws Exception {
        // Test that modulo has the same precedence as multiplication and division
        Expression e = StringToExpression.parseStringTExpression("10 + 15 % 4 * 2");
        Object result = calc.eval(e);
        
        // 10 + 15 % 4 * 2 = 10 + (15 % 4) * 2 = 10 + 3 * 2 = 10 + 6 = 16
        assertTrue(result instanceof Number);
        assertEquals(16, ((Number)result).intValue());
        
        e = StringToExpression.parseStringTExpression("10 * 2 % 7");
        result = calc.eval(e);
        
        // 10 * 2 % 7 = 20 % 7 = 6
        assertTrue(result instanceof Number);
        assertEquals(6, ((Number)result).intValue());
    }
    
    @Test
    void testConstructor() {
        // It should not be possible to create an expression with null parameter list
        assertThrows(IllegalConstruction.class, () -> new Modulo(null));
    }
    
    @Test
    void testNotation() throws Exception {
        // Test different notations
        List<Expression> params = Arrays.asList(new MyNumber(10), new MyNumber(3));
        Modulo modulo = new Modulo(params, Notation.PREFIX);
        assertEquals("% (10, 3)", modulo.toString());
        
        modulo = new Modulo(params, Notation.INFIX);
        assertEquals("( 10 % 3 )", modulo.toString());
        
        modulo = new Modulo(params, Notation.POSTFIX);
        assertEquals("(10, 3) %", modulo.toString());
    }
    
    @Test
    void testEquals() throws IllegalConstruction {
        // Two similar expressions, constructed separately (and using different constructors) should be equal
        List<Expression> p1 = Arrays.asList(new MyNumber(10), new MyNumber(3));
        List<Expression> p2 = Arrays.asList(new MyNumber(10), new MyNumber(3));
        Modulo m1 = new Modulo(p1);
        Modulo m2 = new Modulo(p2, Notation.INFIX);
        
        assertEquals(m1, m2);
        assertEquals(m2, m2); // Reflexivity
        assertNotEquals(m1, new Plus(Arrays.asList(new MyNumber(10), new MyNumber(3))));
    }
    
    @Test
    void testHashCode() throws IllegalConstruction {
        // Two similar expressions, constructed separately (and using different constructors) should have the same hashcode
        List<Expression> p1 = Arrays.asList(new MyNumber(10), new MyNumber(3));
        List<Expression> p2 = Arrays.asList(new MyNumber(10), new MyNumber(3));
        Modulo m1 = new Modulo(p1);
        Modulo m2 = new Modulo(p2, Notation.INFIX);
        
        assertEquals(m1.hashCode(), m2.hashCode());
    }
}