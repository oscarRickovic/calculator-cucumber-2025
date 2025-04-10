package calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

// Importing local packages
import calculator.StaticClasses.Parsers.StringToExpression;
import calculator.Calculator;

public class CalculatorUI extends Application {
    private TextField display;
    private StringBuilder currentInput = new StringBuilder();
    private Calculator c = new Calculator();
    private boolean isScientificMode = false;
    private GridPane scientificGrid;
    private Stage primaryStage;
    private HBox mainLayout;
    private ToggleButton scientificToggle;
    
    // Constants for UI design
    private static final String NUMBER_BUTTON_STYLE = "-fx-background-color: #f0f0f0; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-background-radius: 5;";
    private static final String OPERATION_BUTTON_STYLE = "-fx-background-color: #ff9500; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;";
    private static final String SCIENTIFIC_BUTTON_STYLE = "-fx-background-color: #4a4a4a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;";
    private static final String TOGGLE_BUTTON_STYLE = "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;";
    private static final String TOGGLE_BUTTON_SELECTED_STYLE = "-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;";
    private static final String DISPLAY_STYLE = "-fx-background-color: #f8f8f8; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 5;";
    
    // Button sizes
    private static final double BASIC_BUTTON_SIZE = 60;
    private static final double SCIENTIFIC_BUTTON_SIZE = 70;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("JavaFX Calculator");

        // Create display with improved styling
        display = new TextField();
        display.setEditable(false);
        display.setPrefHeight(60);
        display.setFont(Font.font("Arial", 20));
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setStyle(DISPLAY_STYLE);
        
        // Set initial display width
        display.setPrefWidth(4 * BASIC_BUTTON_SIZE + 30); // Account for padding

        // Create toggle button for scientific mode with improved styling
        scientificToggle = new ToggleButton("Scientific Mode");
        scientificToggle.setStyle(TOGGLE_BUTTON_STYLE);
        scientificToggle.setPrefWidth(4 * BASIC_BUTTON_SIZE + 30);
        scientificToggle.setPrefHeight(40);
        scientificToggle.setOnAction(e -> {
            isScientificMode = scientificToggle.isSelected();
            if (isScientificMode) {
                scientificToggle.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
            } else {
                scientificToggle.setStyle(TOGGLE_BUTTON_STYLE);
            }
            updateLayout();
        });

        // Create basic button grid
        GridPane basicGrid = createBasicButtonGrid();
        
        // Create scientific button grid
        scientificGrid = createScientificButtonGrid();
        
        // Create control panel with toggle button
        VBox controlPanel = new VBox(10);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.getChildren().add(scientificToggle);
        
        // Create the basic calculator box
        VBox basicCalculatorBox = new VBox(10);
        basicCalculatorBox.setAlignment(Pos.CENTER);
        basicCalculatorBox.setPadding(new Insets(15));
        basicCalculatorBox.getChildren().addAll(display, controlPanel, basicGrid);
        
        // Create the main layout as HBox to place scientific grid on the right
        mainLayout = new HBox(15);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().add(basicCalculatorBox);
        
        // Apply a nice background color to the scene
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #f5f7fa, #c3cfe2);");
        
        // Initial scene setup
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateLayout() {
        // If scientific mode is enabled, add the scientific grid if it's not already there
        if (isScientificMode) {
            if (!mainLayout.getChildren().contains(scientificGrid)) {
                mainLayout.getChildren().add(scientificGrid);
                
                // Increase the width of the display when in scientific mode
                display.setPrefWidth((4 * BASIC_BUTTON_SIZE + 30) + (4 * SCIENTIFIC_BUTTON_SIZE + 30));
                
                // Update window size for scientific calculator
                primaryStage.setWidth(4 * BASIC_BUTTON_SIZE + 4 * SCIENTIFIC_BUTTON_SIZE + 90); // Additional space for padding
                primaryStage.setHeight(6 * BASIC_BUTTON_SIZE + 120); // Adjust height for larger buttons
            }
        } else {
            // If scientific mode is disabled, remove the scientific grid if it's there
            mainLayout.getChildren().remove(scientificGrid);
            
            // Restore the original width of the display
            display.setPrefWidth(4 * BASIC_BUTTON_SIZE + 30);
            
            // Restore original window size for basic calculator
            primaryStage.setWidth(4 * BASIC_BUTTON_SIZE + 60);
            primaryStage.setHeight(6 * BASIC_BUTTON_SIZE + 120);
        }
        
        // Force layout update
        primaryStage.sizeToScene();
    }

    private GridPane createBasicButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        String[][] buttonData = {
                {"7", "8", "9", "/", NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, OPERATION_BUTTON_STYLE},
                {"4", "5", "6", "*", NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, OPERATION_BUTTON_STYLE},
                {"1", "2", "3", "-", NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, OPERATION_BUTTON_STYLE},
                {"0", ".", "C", "+", NUMBER_BUTTON_STYLE, NUMBER_BUTTON_STYLE, OPERATION_BUTTON_STYLE, OPERATION_BUTTON_STYLE},
                {"=", "DEL", null, null, OPERATION_BUTTON_STYLE, OPERATION_BUTTON_STYLE, null, null}
        };

        for (int row = 0; row < buttonData.length; row++) {
            for (int col = 0; col < 4; col++) {
                if (buttonData[row][col] != null) {
                    String label = buttonData[row][col];
                    String style = buttonData[row][col + 4];
                    
                    Button button = createStyledButton(label, style, BASIC_BUTTON_SIZE);
                    
                    // Make the equal button wider (span 2 columns)
                    if (label.equals("=")) {
                        grid.add(button, col, row, 2, 1);
                        // Skip the next column since it's covered by the wider button
                        col++;
                    } else {
                        grid.add(button, col, row);
                    }
                }
            }
        }

        return grid;
    }

    private GridPane createScientificButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Scientific buttons with their styles
        String[][] buttonData = {
            {"sin", "cos", "tan", "π", SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE},
            {"asin", "acos", "atan", "e", SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE},
            {"log", "ln", "x²", "x³", SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE},
            {"√x", "∛x", "xʸ", "1/x", SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE},
            {"(", ")", "!", "%", SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE, SCIENTIFIC_BUTTON_STYLE}
        };

        for (int row = 0; row < buttonData.length; row++) {
            for (int col = 0; col < 4; col++) {
                String label = buttonData[row][col];
                String style = buttonData[row][col + 4];
                Button button = createStyledButton(label, style, SCIENTIFIC_BUTTON_SIZE);
                grid.add(button, col, row);
            }
        }

        return grid;
    }

    private Button createStyledButton(String label, String style, double size) {
        Button button = new Button(label);
        button.setPrefSize(size, size);
        button.setFont(Font.font("Arial", 14));
        button.setStyle(style);
        
        // Add drop shadow effect for 3D look
        DropShadow shadow = new DropShadow();
        shadow.setRadius(3.0);
        shadow.setOffsetX(1.0);
        shadow.setOffsetY(1.0);
        shadow.setColor(Color.color(0.4, 0.4, 0.4, 0.3));
        button.setEffect(shadow);
        
        // Add hover effect
        button.setOnMouseEntered(e -> {
            button.setStyle(style + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(style);
        });
        
        // Add click effect
        button.setOnMousePressed(e -> {
            button.setStyle(style + "-fx-scale-x: 0.95; -fx-scale-y: 0.95;");
        });
        
        button.setOnMouseReleased(e -> {
            button.setStyle(style);
        });
        
        button.setOnAction(e -> handleButtonClick(label));
        return button;
    }

    private void handleButtonClick(String label) {
        // Use existing functionality for basic buttons
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
            case "DEL":
                handleDelete();
                break;
            case "x²":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText(currentValue + "²");
                    // Calculation will be done when pressing "="
                }
                break;
            case "x³":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText(currentValue + "³");
                    // Calculation will be done when pressing "="
                }
                break;
            case "√x":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText("√" + currentValue);
                    // Calculation will be done when pressing "="
                }
                break;
            case "∛x":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText("∛" + currentValue);
                    // Calculation will be done when pressing "="
                }
                break;
            case "1/x":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText("1/" + currentValue);
                    // Calculation will be done when pressing "="
                }
                break;
            case "sin":
            case "cos":
            case "tan":
            case "asin":
            case "acos":
            case "atan":
            case "log":
            case "ln":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText(label + "(" + currentValue + ")");
                    // Calculation will be done when pressing "="
                } else {
                    display.setText(label + "()");
                }
                break;
            case "π":
                currentInput.append("π");
                display.setText(currentInput.toString());
                break;
            case "e":
                currentInput.append("e");
                display.setText(currentInput.toString());
                break;
            case "(":
            case ")":
            case "!":
            case "%":
                currentInput.append(label);
                display.setText(currentInput.toString());
                break;
            case "xʸ":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText(currentValue + "^");
                    
                    // Very important: clear the current input so that 
                    // the user can enter the exponent
                    currentInput.setLength(0);
                    
                    // Store the base and operation to rebuild the complete expression
                    currentInput.append(currentValue).append("^");
                }
                break;
            case "MC":  // Memory Clear
            case "MR":  // Memory Recall
            case "M+":  // Memory Add
            case "M-":  // Memory Subtract
            case "DEG": // Degree mode toggle
                // Display that these functions are work in progress
                display.setText(label + " (WIP)");
                break;
            default:
                // For other scientific buttons that are not yet handled
                if (isScientificButton(label)) {
                    if (currentInput.length() > 0) {
                        String currentValue = currentInput.toString();
                        display.setText(label + "(" + currentValue + ")");
                    } else {
                        display.setText(label);
                    }
                } else {
                    // Normal behavior for basic calculator buttons
                    currentInput.append(label);
                    display.setText(currentInput.toString());
                }
        }
    }

    private boolean isScientificButton(String label) {
        String[] scientificLabels = {"sin", "cos", "tan", "asin", "acos", "atan", 
                                     "log", "ln", "x²", "x³", "xʸ", "√x", "∛x", "1/x", 
                                     "π", "e", "(", ")", "!", "%", "MC", "MR", "M+", "M-", "DEG", "RAD", "GRAD"};
        
        for (String sciLabel : scientificLabels) {
            if (sciLabel.equals(label)) {
                return true;
            }
        }
        return false;
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
            // Note: This method should be extended to handle scientific notations
            // like "9²", "√9", etc. For now, it only uses the existing parser
            String result = c.eval(StringToExpression.parseStringTExpression(currentInput.toString())).toString();
            display.setText(result);
            currentInput.setLength(0);
            currentInput.append(result);
        } catch (Exception e) {
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