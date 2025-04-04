package calculator.controller;

import calculator.Calculator;
import calculator.Expression;
import calculator.StaticClasses.Parsers.StringToExpression;
import calculator.dto.ExpressionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final Calculator calculator = new Calculator();
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);



    // ✅ POST /evaluate (Body: { "expression": "3+5*2" })
    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluateExpressionPost(@RequestBody ExpressionDTO expressionDTO) {
        return processExpression(expressionDTO.getExpression());
    }

    // 🔁 Logique partagée POST
    private ResponseEntity<?> processExpression(String expression) {
        logger.info("Expression reçue: {}", expression);

        try {

            if (!expression.matches("[0-9+\\-*/().]+")) {
                logger.error("❌ Expression invalide détectée: {}", expression);
                return ResponseEntity.badRequest().body("Expression invalide.");
            }
           
            Expression parsedExpression = StringToExpression.parseStringTExpression(expression);
            logger.info("Expression parsée avec succès: {}", parsedExpression);

            // Évaluer l'expression
            String result = calculator.eval(parsedExpression).toString();
            logger.info("Résultat calculé: {}", result);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'évaluation de l'expression: ", e);
            return ResponseEntity.status(500).body("Erreur interne : " + e.getMessage());
        }
    }
}
