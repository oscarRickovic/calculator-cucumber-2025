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
        
        Expression e = StringToExpression.parseStringTExpression("{(4 * 3) + [2 * 5]} / 5)))");
        int result = c.eval(e).intValue(); 
        assertEquals(4, result);


        e = StringToExpression.parseStringTExpression("50 - 3 * 4 + 10 - 2 * 5 + 8 * 2");
        result = c.eval(e).intValue(); 
        assertEquals(54, result);

        e = StringToExpression.parseStringTExpression("(10 + 5) * 3 - (8 - 2) * 4 + 6");
        result = c.eval(e).intValue(); 
        assertEquals(27, result);

        e = StringToExpression.parseStringTExpression("((10 + 2) * 3 - 5) + ((8 - 4) * 6) - (7 * 2) + 9");
        result = c.eval(e).intValue(); 
        assertEquals(50, result);


        e = StringToExpression.parseStringTExpression("10 * 3 - 5 * 2");
        result = c.eval(e).intValue(); 
        assertEquals(20, result);


        e = StringToExpression.parseStringTExpression("5 + 10 * 2 - 3");
        result = c.eval(e).intValue(); 
        assertEquals(22, result);


        e = StringToExpression.parseStringTExpression("(5 + 3) * 2 - (4 - 2) * 3");
        result = c.eval(e).intValue(); 
        assertEquals(10, result);

        e = StringToExpression.parseStringTExpression("1000 - 5 * 200 + 50");
        result = c.eval(e).intValue(); 
        assertEquals(50, result);

        e = StringToExpression.parseStringTExpression("((10 + 5) * (3 - 2) + (8 * 2)) - (6 * (4 - 2)) + 9");
        result = c.eval(e).intValue(); 
        assertEquals(28, result);

        e = StringToExpression.parseStringTExpression("(3 + 5) * (9 - 2) ");
        result = c.eval(e).intValue(); 
        assertEquals(56, result);

        e = StringToExpression.parseStringTExpression("1+2");
        result = c.eval(e).intValue(); 
        assertEquals(3, result);

        e = StringToExpression.parseStringTExpression("3*7");
        result = c.eval(e).intValue(); 
        assertEquals(21, result);
     
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
        int result = c.eval(e).intValue(); 
        assertEquals(6, result);


        e = StringToExpression.parseStringTExpression("4 - - - 2");
        result = c.eval(e).intValue(); 
        assertEquals(2, result);

        e = StringToExpression.parseStringTExpression("-2 + 4");
        result = c.eval(e).intValue(); 
        assertEquals(2, result);

        e = StringToExpression.parseStringTExpression("-2 + - 2");
        result = c.eval(e).intValue(); 
        assertEquals(-4, result);


        e = StringToExpression.parseStringTExpression("(1-(-(-(-1))))");
        result = c.eval(e).intValue(); 
        assertEquals(2, result);
     
    }
    
}
