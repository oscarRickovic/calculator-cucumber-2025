package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A user interface for solving linear equations using the LinearEquationSolver.
 */
public class LinearEquationSolverUI {
    
    private Stage stage;
    private VBox equationsContainer;
    private TextArea resultArea;
    private List<TextField> equationFields;
    private int currentEquations;
    
    // Constants for UI design - Light Mode
    private static final String LIGHT_BG = "linear-gradient(to bottom right, #ffffff, #f5f5f5)";
    private static final String LIGHT_BUTTON_STYLE = "-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_DANGER_BUTTON_STYLE = "-fx-background-color: #ea4335; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_SUCCESS_BUTTON_STYLE = "-fx-background-color: #34a853; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_TEXTFIELD_STYLE = "-fx-background-color: #ffffff; -fx-text-fill: #333333; -fx-border-color: #dddddd; -fx-border-width: 1; -fx-border-radius: 4;";
    private static final String LIGHT_RESULT_STYLE = "-fx-background-color: #ffffff; -fx-text-fill: #333333; -fx-border-color: #dddddd; -fx-border-width: 1; -fx-border-radius: 4;";
    
    /**
     * Creates a new LinearEquationSolverUI.
     */
    public LinearEquationSolverUI() {
        this.equationFields = new ArrayList<>();
        this.currentEquations = 1; // Start with one equation
    }
    
    /**
     * Shows the equation solver window.
     */
    public void showSolver() {
        if (stage == null) {
            stage = new Stage();
            stage.setTitle("Linear Equation Solver");
            
            // Create the root layout
            BorderPane root = new BorderPane();
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: " + LIGHT_BG + ";");
            
            // Title and instructions
            Label titleLabel = new Label("Linear Equation Solver");
            titleLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
            
            Label instructionsLabel = new Label("Enter linear equations in the form: ax + by + c = dx + ey + f");
            instructionsLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 14));
            
            VBox headerBox = new VBox(10, titleLabel, instructionsLabel);
            headerBox.setAlignment(Pos.CENTER);
            root.setTop(headerBox);
            
            // Equations container
            equationsContainer = new VBox(10);
            equationsContainer.setPadding(new Insets(20, 0, 20, 0));
            ScrollPane scrollPane = new ScrollPane(equationsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent;");
            
            // Add the initial equation field
            addEquationField();
            
            // Buttons for managing equations
            Button addButton = new Button("Add Equation");
            addButton.setStyle(LIGHT_BUTTON_STYLE);
            addButton.setOnAction(e -> addEquationField());
            
            Button removeButton = new Button("Remove Last Equation");
            removeButton.setStyle(LIGHT_DANGER_BUTTON_STYLE);
            removeButton.setOnAction(e -> removeLastEquation());
            
            Button solveButton = new Button("Solve");
            solveButton.setStyle(LIGHT_SUCCESS_BUTTON_STYLE);
            solveButton.setOnAction(e -> solveEquations());
            
            Button clearButton = new Button("Clear All");
            clearButton.setStyle(LIGHT_DANGER_BUTTON_STYLE);
            clearButton.setOnAction(e -> clearAll());
            
            HBox buttonBox = new HBox(10, addButton, removeButton, solveButton, clearButton);
            buttonBox.setAlignment(Pos.CENTER);
            
            // Results area
            resultArea = new TextArea();
            resultArea.setStyle(LIGHT_RESULT_STYLE);
            resultArea.setEditable(false);
            resultArea.setPrefHeight(150);
            resultArea.setFont(Font.font("Monospace", 14));
            resultArea.setWrapText(true);
            
            // Combine all elements in center
            VBox centerBox = new VBox(10, scrollPane, buttonBox, new Label("Results:"), resultArea);
            root.setCenter(centerBox);
            
            // Create the scene
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.setMinWidth(400);
            stage.setMinHeight(400);
        }
        
        // Show the stage
        stage.show();
        stage.toFront();
    }
    
    /**
     * Adds a new equation input field with a label.
     */
    private void addEquationField() {
        HBox equationBox = new HBox(10);
        equationBox.setAlignment(Pos.CENTER_LEFT);
        
        Label equationLabel = new Label("Equation " + currentEquations + ":");
        equationLabel.setPrefWidth(100);
        
        TextField equationField = new TextField();
        equationField.setStyle(LIGHT_TEXTFIELD_STYLE);
        equationField.setPromptText("Example: 2x + 3y = 5");
        equationField.setPrefWidth(300);
        HBox.setHgrow(equationField, Priority.ALWAYS);
        
        equationBox.getChildren().addAll(equationLabel, equationField);
        equationsContainer.getChildren().add(equationBox);
        
        equationFields.add(equationField);
        currentEquations++;
    }
    
    /**
     * Removes the last equation field.
     */
    private void removeLastEquation() {
        if (currentEquations > 2) { // Keep at least one equation
            equationsContainer.getChildren().remove(equationsContainer.getChildren().size() - 1);
            equationFields.remove(equationFields.size() - 1);
            currentEquations--;
        } else {
            showAlert("Cannot remove", "At least one equation is required.");
        }
    }
    
    /**
     * Solves the system of equations.
     */
    private void solveEquations() {
        try {
            // Collect non-empty equations
            List<String> equations = new ArrayList<>();
            for (TextField field : equationFields) {
                String equation = field.getText().trim();
                if (!equation.isEmpty()) {
                    equations.add(equation);
                }
            }
            
            if (equations.isEmpty()) {
                showAlert("No Equations", "Please enter at least one equation.");
                return;
            }
            
            // Convert to array
            String[] equationArray = equations.toArray(new String[0]);
            
            // Solve based on number of equations
            if (equations.size() == 1) {
                // Single variable equation
                String result = LinearEquationSolver.solveSingleVariable(equations.get(0));
                resultArea.setText("Solution:\n" + result);
            } else {
                // System of equations
                try {
                    Map<String, Double> solutions = LinearEquationSolver.solveSystem(equationArray);
                    
                    // Format the results
                    StringBuilder resultText = new StringBuilder("Solution:\n");
                    for (Map.Entry<String, Double> entry : solutions.entrySet()) {
                        String varName = entry.getKey();
                        double value = entry.getValue();
                        
                        // Format the value (handle integers and decimals)
                        String formattedValue;
                        if (Math.abs(value - Math.round(value)) < 1e-10) {
                            formattedValue = Long.toString(Math.round(value));
                        } else {
                            formattedValue = String.format("%.6f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
                        }
                        
                        resultText.append(varName).append(" = ").append(formattedValue).append("\n");
                    }
                    
                    resultArea.setText(resultText.toString());
                } catch (IllegalArgumentException e) {
                    resultArea.setText("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }
    
    /**
     * Clears all equation fields.
     */
    private void clearAll() {
        for (TextField field : equationFields) {
            field.clear();
        }
        resultArea.clear();
    }
    
    /**
     * Shows an alert dialog with the given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}