package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import calculator.StaticClasses.Parsers.StringToExpression;

/**
 * Integration test for mixed expressions involving complex numbers and functions.
 */
public class TestMixedExpressions {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testRealNumbersWithFunctions() throws Exception {
        // Test various expressions with real numbers and functions
        String[] expressions = {
            "1 + sin(0) + 2",
            "cos(0) * 5",
            "10 / exp(0)",
            "ln(exp(3))",
            "sin(cos(ln(exp(1))))",
            "2 * sin(3.14159/6) + 3",
            "(4 - 2) * cos(0) / 2"
        };
        
        double[] expectedResults = {
            3.0,      // 1 + sin(0) + 2 = 1 + 0 + 2 = 3
            5.0,      // cos(0) * 5 = 1 * 5 = 5
            10.0,     // 10 / exp(0) = 10 / 1 = 10
            3.0,      // ln(exp(3)) = 3
            0.51439,   // sin(cos(ln(exp(1)))) = 0.51439
            4.0,      // 2 * sin(3.14159/6) + 3 = 2 * 0.5 + 3 = 1 + 3 = 4
            1.0       // (4 - 2) * cos(0) / 2 = 2 * 1 / 2 = 1
        };
        
        for (int i = 0; i < expressions.length; i++) {
            Expression e = StringToExpression.parseStringTExpression(expressions[i]);
            Object result = calc.eval(e);
            
            assertTrue(result instanceof Number, "Result of " + expressions[i] + " should be a number");
            assertEquals(expectedResults[i], ((Number)result).doubleValue(), 0.001,
                    "Expression " + expressions[i] + " should evaluate to " + expectedResults[i]);
        }
    }
    
    @Test
    void testComplexNumbersWithFunctions() throws Exception {
        // Test sin and cos with pure imaginary numbers
        Expression e = StringToExpression.parseStringTExpression("sin(i)");
        Object result = calc.eval(e);
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        // sin(i) = i * sinh(1) ≈ i * 1.1752
        assertEquals(0.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(1.1752, complex.getImaginaryPart().doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("cos(i)");
        result = calc.eval(e);
        assertTrue(result instanceof MyComplexNumber);
        complex = (MyComplexNumber) result;
        // cos(i) = cosh(1) ≈ 1.5431
        assertEquals(1.5431, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(0.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testMixedExpressionsWithFunctionsAndComplex() throws Exception {
        // Test expressions that mix complex numbers and functions
        Expression e = StringToExpression.parseStringTExpression("1 + 2i + sin(0)");
        Object result = calc.eval(e);
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        // 1 + 2i + sin(0) = 1 + 2i + 0 = 1 + 2i
        assertEquals(1.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(2.0, complex.getImaginaryPart().doubleValue(), 0.0001);
        
        e = StringToExpression.parseStringTExpression("(3 + 4i) * cos(0)");
        result = calc.eval(e);
        assertTrue(result instanceof MyComplexNumber);
        complex = (MyComplexNumber) result;
        // (3 + 4i) * cos(0) = (3 + 4i) * 1 = 3 + 4i
        assertEquals(3.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(4.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testEulerIdentity() throws Exception {
        // Test Euler's identity: e^(i*π) + 1 = 0
        Expression e = StringToExpression.parseStringTExpression("exp(i*3.14159) + 1");
        Object result = calc.eval(e);
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        // Due to floating point precision, we expect something very close to 0
        assertEquals(0.0, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(0.0, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testFunctionWithComplexArgument() throws Exception {
        // Test function with complex argument specified as a comma-separated pair
        Expression e = StringToExpression.parseStringTExpression("sin(3+5i)");
        Object result = calc.eval(e);
        
        // The parser should interpret this as sin(3+5i)
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        
        // Now compute the expected result for sin(3+5i)
        // sin(a+bi) = sin(a)cosh(b) + i*cos(a)sinh(b)
        double expectedReal = Math.sin(3) * Math.cosh(5);
        double expectedImag = Math.cos(3) * Math.sinh(5);
        
        assertEquals(expectedReal, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(expectedImag, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testComplexExpressionWithFunctions() throws Exception {
        // Test a more complex expression with multiple operations and functions
        Expression e = StringToExpression.parseStringTExpression("1 + 2i + sin(3+5i)");
        Object result = calc.eval(e);
        
        assertTrue(result instanceof MyComplexNumber);
        MyComplexNumber complex = (MyComplexNumber) result;
        
        // Expected: 1 + 2i + sin(3+5i)
        double sinReal = Math.sin(3) * Math.cosh(5);
        double sinImag = Math.cos(3) * Math.sinh(5);
        
        assertEquals(1 + sinReal, complex.getRealPart().doubleValue(), 0.0001);
        assertEquals(2 + sinImag, complex.getImaginaryPart().doubleValue(), 0.0001);
    }
    
    @Test
    void testInvalidFunctionExpressions() {
        // Test invalid expressions with functions
        
        // Missing argument
        assertThrows(Exception.class, () -> 
            StringToExpression.parseStringTExpression("sin()"));
            
        // Invalid argument (not a number)
        assertThrows(Exception.class, () -> 
            StringToExpression.parseStringTExpression("sin(x)"));
            
        // Missing closing parenthesis
        assertThrows(Exception.class, () -> 
            StringToExpression.parseStringTExpression("sin(3"));
            
        // Nested unclosed parentheses
        assertThrows(Exception.class, () -> 
            StringToExpression.parseStringTExpression("sin(cos(ln(3)"));
    }
}