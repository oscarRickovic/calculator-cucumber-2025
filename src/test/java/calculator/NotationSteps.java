package calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import io.cucumber.java.en.*;

public class NotationSteps {
    private int value1, value2;
    private Operation operation;
    
    @Given("I have numbers {int} and {int}")
    public void i_have_numbers(int num1, int num2) {
        this.value1 = num1;
        this.value2 = num2;
    }

    @When("I perform {string} operation")
    public void i_perform_operation(String symbol) {
        List<Expression> params = Arrays.asList(new MyNumber(value1), new MyNumber(value2));
        try {
            switch (symbol) {
                case "+" -> operation = new Plus(params);
                case "-" -> operation = new Minus(params);
                case "*" -> operation = new Times(params);
                case "/" -> operation = new Divides(params);
                default -> fail("Invalid operation symbol");
            }
        } catch (IllegalConstruction e) {
            fail("Operation construction failed");
        }
    }

    @Then("the notation output should be correctly formatted")
    public void the_notation_output_should_be_correctly_formatted() throws IllegalConstruction {
        assertNotationCorrect("+", new Plus(Arrays.asList(new MyNumber(value1), new MyNumber(value2))));
        assertNotationCorrect("-", new Minus(Arrays.asList(new MyNumber(value1), new MyNumber(value2))));
        assertNotationCorrect("*", new Times(Arrays.asList(new MyNumber(value1), new MyNumber(value2))));
        assertNotationCorrect("/", new Divides(Arrays.asList(new MyNumber(value1), new MyNumber(value2))));
    }

    private void assertNotationCorrect(String symbol, Operation op) {
        assertEquals(symbol + " (" + value1 + ", " + value2 + ")", op.toString(Notation.PREFIX));
        assertEquals("( " + value1 + " " + symbol + " " + value2 + " )", op.toString(Notation.INFIX));
        assertEquals("(" + value1 + ", " + value2 + ") " + symbol, op.toString(Notation.POSTFIX));
    }
}
