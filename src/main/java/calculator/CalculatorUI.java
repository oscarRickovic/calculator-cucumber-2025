package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// adding local packages 
import calculator.StaticClasses.Parsers.StringToExpression;
import calculator.Calculator;

public class CalculatorUI extends Application {
    private TextField display;
    private StringBuilder currentInput = new StringBuilder();
    private Calculator c = new Calculator(); 

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Calculator");

        display = new TextField();
        display.setEditable(false);
        display.setPrefHeight(50);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        int row = 1, col = 0;
        for (String label : buttons) {
            Button button = new Button(label);
            button.setPrefSize(50, 50);
            button.setOnAction(e -> handleButtonClick(label));
            grid.add(button, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        // Ajout du bouton "⌫" (Backspace) pour supprimer le dernier caractère
        Button deleteButton = new Button("del");
        deleteButton.setPrefSize(50, 50);
        deleteButton.setOnAction(e -> handleDelete());
        grid.add(deleteButton, 3, row); // Placement à la fin

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

    private void handleButtonClick(String label) {
        if (label.equals("C")) {
            currentInput.setLength(0);
        } else if (label.equals("=")) {
            try {
                String result = Integer.toString(c.eval(StringToExpression.parseStringTExpression(currentInput.toString())));
                display.setText(result);
                currentInput.setLength(0);
                currentInput.append(result);
                return;
            } catch (Exception e) {
                // do nothing
            }
        } else {
            currentInput.append(label);
        }
        display.setText(currentInput.toString());
    }

    private void handleDelete() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            display.setText(currentInput.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}