package calculator.controller;

import java.util.List;

public class ExpressionRequest {

    private List<Double> numbers;

    // Getter pour 'numbers'
    public List<Double> getNumbers() {
        return numbers;
    }

    // Setter pour 'numbers'
    public void setNumbers(List<Double> numbers) {
        this.numbers = numbers;
    }
}
