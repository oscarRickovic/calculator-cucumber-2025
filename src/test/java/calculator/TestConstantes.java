package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for mathematical constants.
 */
public class TestConstantes {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testMathConstantCreation() {
        MathConstant pi = new MathConstant("PI");
        assertEquals(Math.PI, pi.getValue().doubleValue(), 0.0001);
        assertEquals("PI", pi.getConstantName());
        
        MathConstant e = new MathConstant("E");
        assertEquals(Math.E, e.getValue().doubleValue(), 0.0001);
        assertEquals("E", e.getConstantName());
        
        MathConstant phi = new MathConstant("PHI");
        assertEquals((1 + Math.sqrt(5)) / 2.0, phi.getValue().doubleValue(), 0.0001);
        assertEquals("PHI", phi.getConstantName());
        
        MathConstant sqrt2 = new MathConstant("SQRT2");
        assertEquals(Math.sqrt(2), sqrt2.getValue().doubleValue(), 0.0001);
        assertEquals("SQRT2", sqrt2.getConstantName());
    }
    
    @Test
    void testInvalidConstant() {
        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> new MathConstant("INVALID_CONSTANT"));
        assertTrue(exception.getMessage().contains("Unknown mathematical constant"));
    }
    
    @Test
    void testCaseInsensitivity() {
        MathConstant pi1 = new MathConstant("PI");
        MathConstant pi2 = new MathConstant("pi");
        
        assertEquals(pi1.getValue().doubleValue(), pi2.getValue().doubleValue(), 0.0001);
        assertEquals("PI", pi2.getConstantName()); // Should be normalized to uppercase
    }
    
    @Test
    void testMathConstantsWithParser() throws Exception {
        // Test basic constant parsing
        Expression e = StringToExpression.parseStringTExpression("PI");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(Math.PI, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("E");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(Math.E, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testMathConstantsInExpressions() throws Exception {
        // Test constants in mathematical expressions
        Expression e = StringToExpression.parseStringTExpression("2 * PI");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(2 * Math.PI, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("PI + E");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(Math.PI + Math.E, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testMathConstantsWithFunctions() throws Exception {
        // Test constants with functions
        Expression e = StringToExpression.parseStringTExpression("sin(PI/2)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("cos(PI)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(-1.0, ((Number)result).doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("ln(E)");
        result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(1.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testNegativeConstant() throws Exception {
        // Test negative PI
        Expression e = StringToExpression.parseStringTExpression("-PI");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(-Math.PI, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testMathConstantProperties() throws Exception {
        // Test that E^x = exp(x)
        Expression e1 = StringToExpression.parseStringTExpression("E^2");
        Expression e2 = StringToExpression.parseStringTExpression("exp(2)");
        
        Object result1 = calc.eval(e1);
        Object result2 = calc.eval(e2);
        
        assertEquals(((Number)result1).doubleValue(), ((Number)result2).doubleValue(), 0.0001);
        
        // Test golden ratio property: φ² = φ + 1, so φ² - φ - 1 = 0
        Expression e = StringToExpression.parseStringTExpression("PHI^2 - PHI - 1");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof Number);
        assertEquals(0.0, ((Number)result).doubleValue(), 0.0001);
    }
    
    @Test
    void testCountDepthOpsAndNbs() {
        MathConstant pi = new MathConstant("PI");
        
        // MathConstant should behave like MyNumber for these methods
        assertEquals(0, pi.countDepth());
        assertEquals(0, pi.countOps());
        assertEquals(1, pi.countNbs());
    }
    
    @Test
    void testEqualsAndHashCode() {
        MathConstant pi1 = new MathConstant("PI");
        MathConstant pi2 = new MathConstant("PI");
        MathConstant e = new MathConstant("E");
        
        assertEquals(pi1, pi2);
        assertNotEquals(pi1, e);
        assertEquals(pi1.hashCode(), pi2.hashCode());
        assertNotEquals(pi1.hashCode(), e.hashCode());
        
        // Also test with null and different types
        assertNotEquals(pi1, null);
        assertNotEquals(pi1, "PI");
    }
}