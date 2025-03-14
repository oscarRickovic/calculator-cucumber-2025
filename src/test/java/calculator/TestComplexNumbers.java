package calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for complex number operations in the calculator.
 */
public class TestComplexNumbers {

    private Calculator calc;
    
    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }
    
    @Test
    void testComplexNumberCreation() {
        MyComplexNumber z1 = new MyComplexNumber(3.3, 4);
        assertEquals(3.3, z1.getRealPart().doubleValue());
        assertEquals(4.0, z1.getImaginaryPart().doubleValue());
        assertEquals("3.3+4.0i", z1.toString());
        
        MyComplexNumber z2 = new MyComplexNumber(5.2, -2);
        assertEquals(5.2, z2.getRealPart().doubleValue());
        assertEquals(-2.0, z2.getImaginaryPart().doubleValue());
        assertEquals("5.2-2.0i", z2.toString());
        
        MyComplexNumber z3 = new MyComplexNumber(7.5, 0);
        assertEquals(7.5, z3.getRealPart().doubleValue());
        assertEquals(0.0, z3.getImaginaryPart().doubleValue());
        assertEquals("7.5", z3.toString());
        
        MyComplexNumber z4 = new MyComplexNumber(0, 9);
        assertEquals(0.0, z4.getRealPart().doubleValue());
        assertEquals(9.0, z4.getImaginaryPart().doubleValue());
        assertEquals("9.0i", z4.toString());
    }
    
    @Test
    void testComplexNumberEquality() {
        MyComplexNumber z1 = new MyComplexNumber(3, 4);
        MyComplexNumber z2 = new MyComplexNumber(3, 4);
        MyComplexNumber z3 = new MyComplexNumber(3, 5);
        MyComplexNumber z4 = new MyComplexNumber(4, 4);
        
        assertEquals(z1, z2);
        assertNotEquals(z1, z3);
        assertNotEquals(z1, z4);
        assertNotEquals(z3, z4);
        
        // Test with different numeric types
        MyComplexNumber z5 = new MyComplexNumber(3.0, 4.0);
        MyComplexNumber z6 = new MyComplexNumber(3, 4);
        assertEquals(z5, z6);
    }
    
    @Test
    void testBasicComplexNumberAddition() {
        MyComplexNumber z1 = new MyComplexNumber(3, 4);
        MyComplexNumber z2 = new MyComplexNumber(2, -1);
        
        // Manual calculation for addition: (3+4i) + (2-i) = (5+3i)
        assertEquals(5.0, z1.getRealPart().doubleValue() + z2.getRealPart().doubleValue());
        assertEquals(3.0, z1.getImaginaryPart().doubleValue() + z2.getImaginaryPart().doubleValue());
    }
    
    @Test
    void testBasicComplexNumberMultiplication() {
        MyComplexNumber z1 = new MyComplexNumber(3, 2);
        MyComplexNumber z2 = new MyComplexNumber(1, 4);
        
        // Manual calculation for multiplication: (3+2i) * (1+4i) = (3-8) + (12+2)i = -5+14i
        double realPart = z1.getRealPart().doubleValue() * z2.getRealPart().doubleValue() - 
                         z1.getImaginaryPart().doubleValue() * z2.getImaginaryPart().doubleValue();
        double imagPart = z1.getRealPart().doubleValue() * z2.getImaginaryPart().doubleValue() + 
                         z1.getImaginaryPart().doubleValue() * z2.getRealPart().doubleValue();
                         
        assertEquals(-5.0, realPart);
        assertEquals(14.0, imagPart);
    }
    
    @Test
    void testBasicComplexNumberDivision() {
        MyComplexNumber z1 = new MyComplexNumber(3, 2);
        MyComplexNumber z2 = new MyComplexNumber(1, 1);
        
        // Manual calculation for division:
        // (3+2i) / (1+i) = ((3*1 + 2*1)/(1^2 + 1^2)) + ((2*1 - 3*1)/(1^2 + 1^2))i
        // = (5/2) + (-1/2)i = 2.5 + 0.5i
        
        double denominator = z2.getRealPart().doubleValue() * z2.getRealPart().doubleValue() + 
                           z2.getImaginaryPart().doubleValue() * z2.getImaginaryPart().doubleValue();
        
        double realPart = (z1.getRealPart().doubleValue() * z2.getRealPart().doubleValue() + 
                         z1.getImaginaryPart().doubleValue() * z2.getImaginaryPart().doubleValue()) / denominator;
                         
        double imagPart = (z1.getImaginaryPart().doubleValue() * z2.getRealPart().doubleValue() - 
                         z1.getRealPart().doubleValue() * z2.getImaginaryPart().doubleValue()) / denominator;
                         
        assertEquals(2.5, realPart);
        assertEquals(-0.5, imagPart);
    }
}