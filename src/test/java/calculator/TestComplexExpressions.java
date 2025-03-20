package calculator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Test class for parsing and evaluating expressions with complex numbers.
 */
public class TestComplexExpressions {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testSimpleComplexNumber() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("3+4i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(3.0, complex.getRealPart().doubleValue());
        assertEquals(4.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testComplexAddition() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("(3+4i) + (2-i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(5.0, complex.getRealPart().doubleValue());
        assertEquals(3.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testComplexSubtraction() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("(5+3i) - (2+i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(3.0, complex.getRealPart().doubleValue());
        assertEquals(2.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testComplexMultiplication() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("(3+2i) * (1+4i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(-5.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(14.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexDivision() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("(3+2i) / (1+i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(2.5, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(-0.5, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testMixedRealAndComplex() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("5 + (3+2i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(8.0, complex.getRealPart().doubleValue());
        assertEquals(2.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testScalarMultiplication() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("2 * (3+4i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(6.0, complex.getRealPart().doubleValue());
        assertEquals(8.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testJustImaginary() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(0.0, complex.getRealPart().doubleValue());
        assertEquals(1.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testNegativeImaginary() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("-i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(0.0, complex.getRealPart().doubleValue());
        assertEquals(-1.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testComplexWithoutRealPart() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("3i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(0.0, complex.getRealPart().doubleValue());
        assertEquals(3.0, complex.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testNestedComplexExpression() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("((3i)*(4i) / 2)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(-6.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(0.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexPrecedence() throws Exception {
        // This tests that complex operations follow the same precedence rules
        Expression e = StringToExpression.parseStringTExpression("3+4i + (2-i) * (3+2i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(11.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(5.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexWithBrackets() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("(3+4i) * {2-i}");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(10.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(5.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testImaginarySquared() throws Exception {
        // i² = -1
        Expression e = StringToExpression.parseStringTExpression("i * i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(-1.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(0.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    
    @Test
    void testComplexWithDecimalParts() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("3.5+4.2i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(3.5, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(4.2, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testAdditionWithMultipleComplexNumbers() throws Exception {
        Expression e = StringToExpression.parseStringTExpression("1+2i + 3+4i + 5+6i");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        assertEquals(9.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(12.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testDivisionByZeroComplex() {
        assertThrows(Exception.class, () -> {
            Expression e = StringToExpression.parseStringTExpression("(3+2i) / (0+0i)");
            calc.eval(e);
        });
    }
    
    @Test
    void testInvalidComplexFormat() {
        assertThrows(Exception.class, () -> {
            StringToExpression.parseStringTExpression("3+4i+");
        });
        
        assertThrows(Exception.class, () -> {
            StringToExpression.parseStringTExpression("3+i4");
        });
    }
}