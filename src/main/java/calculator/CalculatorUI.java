package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// Importing local packages
import calculator.StaticClasses.Parsers.StringToExpression;
import calculator.Calculator;

public class CalculatorUI extends Application {
    private TextField display;
    private StringBuilder currentInput = new StringBuilder();
    private Calculator c = new Calculator();
    private boolean complexMode = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Calculator");

        display = new TextField();
        display.setEditable(false);
        display.setPrefHeight(50);

        GridPane grid = createButtonGrid();
        ToggleButton toggleComplex = new ToggleButton("Complex Mode");
        toggleComplex.setOnAction(e -> complexMode = toggleComplex.isSelected());
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));
        root.setVgap(10);
        root.add(display, 0, 0);
        root.add(grid, 0, 1);

        Scene scene = new Scene(root, 250, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        String[] buttons = {"7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", "0", ".", "i", "+", "="};

        int row = 1, col = 0;
        for (String label : buttons) {
            Button button = createButton(label);
            grid.add(button, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        // Backspace (Delete) button
        Button deleteButton = createButton("del");
        deleteButton.setOnAction(e -> handleDelete()); // FIX: Link to handleDelete()
        grid.add(deleteButton, 3, row); 

        return grid;
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setPrefSize(50, 50);
        button.setOnAction(e -> handleButtonClick(label));
        return button;
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "C":
                currentInput.setLength(0);
                display.setText("");
                break;
            case "=":
                evaluateExpression();
                break;
            case ".":
                addDecimalPoint();
                break;
            default:
                currentInput.append(label);
                display.setText(currentInput.toString());
        }
    }

    private void addDecimalPoint() {
        if (currentInput.length() == 0 || isLastCharacterOperator()) {
            currentInput.append("0.");  // Ensure proper decimal handling
        } else if (!getLastNumber().contains(".")) {
            currentInput.append(".");
        }
        display.setText(currentInput.toString());
    }

    private void evaluateExpression() {
        try {

            Expression parsedExpression = StringToExpression.parseStringTExpression(currentInput.toString());

            if (parsedExpression == null) {
                display.setText("Parsing Error");
                return;
            }

            String result = c.eval(parsedExpression).toString();

            display.setText(result);
            currentInput.setLength(0);
            currentInput.append(result);
        } catch (Exception e) {
            e.printStackTrace();
            display.setText("Error");
            currentInput.setLength(0);
        }
    }

    private void handleDelete() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            display.setText(currentInput.toString());
        }
    }

    private boolean isLastCharacterOperator() {
        if (currentInput.length() == 0) return true;
        char lastChar = currentInput.charAt(currentInput.length() - 1);
        return "+-*/".indexOf(lastChar) >= 0;
    }

    private String getLastNumber() {
        String[] parts = currentInput.toString().split("[+\\-*/]");
        return parts.length > 0 ? parts[parts.length - 1] : "";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
