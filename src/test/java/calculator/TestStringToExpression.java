package calculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import calculator.StaticClasses.Parsers.StringToExpression;

public class TestStringToExpression {
    
    Calculator c = new Calculator();

    @Test
    public void StringToExpressionPositiveNumbersTest() throws Exception{
        
        Expression e = StringToExpression.parseStringTExpression("{(4 * 3) + [2 * 5]} / 5");
        Object result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(4, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("50 - 3 * 4 + 10 - 2 * 5 + 8 * 2");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(54, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("(10 + 5) * 3 - (8 - 2) * 4 + 6");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(27, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("((10 + 2) * 3 - 5) + ((8 - 4) * 6) - (7 * 2) + 9");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(50, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("10 * 3 - 5 * 2");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(20, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("5 + 10 * 2 - 3");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(22, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("(5 + 3) * 2 - (4 - 2) * 3");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(10, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("1000 - 5 * 200 + 50");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(50, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("((10 + 5) * (3 - 2) + (8 * 2)) - (6 * (4 - 2)) + 9");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(28, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("(3 + 5) * (9 - 2) ");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(56, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("1+2");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(3, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("3*7");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(21, ((Number)result).intValue());
     
    }

    @Test
    public void invalidExpressionTest() {
        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("{(((2"));
            });
        
        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("((3+5)*(9-2)"));
            });
        
        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("a++b"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("(((a"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("5+++++8h"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("hello"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("O+1"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("O"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("o"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("{1+3"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("1+2/"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("2x+y"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("111********333"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("(11+3)++2"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("1/"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("++"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("33*/2"));
            });
            
        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("+4+3"));
            });

        assertThrows(
            Exception.class,
            () -> {
                c.eval(StringToExpression.parseStringTExpression("<<"));
            });
    }

    @Test
    public void StringToExpressionNegativeNumbersTest() throws Exception{
        
        Expression e = StringToExpression.parseStringTExpression("4 -- 2");
        Object result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(6, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("4 - - - 2");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(2, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("-2 + 4");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(2, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("-2 + - 2");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(-4, ((Number)result).intValue());

        e = StringToExpression.parseStringTExpression("(1-(-(-(-1))))");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(2, ((Number)result).intValue());
    }
    
    @Test
    public void StringToExpressionDoubleNumbersTest() throws Exception{
        Expression e = StringToExpression.parseStringTExpression("3.2 + 2.2");
        Object result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(5.4, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("10.5 * 2.0");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(21.0, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("7.8 - 3.3");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(4.5, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("9.6 / 3.2");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(3.0, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("5.5 + 2.5");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(8.0, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("12.75 - 4.25");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(8.5, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("6.0 * 3.5");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(21.0, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("14.4 / 4.8");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(3.0, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("(2.2 + 3.3) * 2.0");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(11.0, ((Number)result).doubleValue(), 0.0001);

        e = StringToExpression.parseStringTExpression("(10.5 - 2.5) / 2.0");
        result = c.eval(e);
        assertTrue(result instanceof Number);
        assertEquals(4.0, ((Number)result).doubleValue(), 0.0001);
    }
}