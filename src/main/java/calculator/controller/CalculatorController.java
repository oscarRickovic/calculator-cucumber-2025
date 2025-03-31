package calculator.controller;

import calculator.Calculator;
import calculator.Expression;
import calculator.MyNumber;
import calculator.Notation;
import calculator.Plus;
import calculator.IllegalConstruction;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final Calculator calculator = new Calculator();

    @GetMapping("/evaluate")
    public String evaluateExpression() {
        return "yes";
    }
}
