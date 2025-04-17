package calculator;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

// Importing local packages
import calculator.StaticClasses.Parsers.StringToExpression;
import calculator.Calculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorUI extends Application {
    private TextField display;
    private Label expressionHistory;
    private StringBuilder currentInput = new StringBuilder();
    private Calculator calculator = new Calculator();
    private boolean isScientificMode = false;
    private SimpleBooleanProperty isDarkMode = new SimpleBooleanProperty(false);
    private GridPane scientificGrid;
    private Stage primaryStage;
    private BorderPane mainLayout;
    private ToggleButton scientificToggle;
    private ToggleButton themeToggle;
    private List<String> calculationHistory = new ArrayList<>();
    private int historyIndex = -1;
    
    // Memory functionality
    private double memoryValue = 0.0;
    private boolean hasMemory = false;
    
    // Constants for UI design - Light Mode
    private static final String LIGHT_BG = "linear-gradient(to bottom right, #ffffff, #f5f5f5)";
    private static final String LIGHT_NUMBER_STYLE = "-fx-background-color: #f8f8f8; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_OPERATION_STYLE = "-fx-background-color: #ff9500; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_SCIENTIFIC_STYLE = "-fx-background-color: #e6e6e6; -fx-text-fill: #333333; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_MEMORY_STYLE = "-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String LIGHT_TOGGLE_STYLE = "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15;";
    private static final String LIGHT_TOGGLE_SELECTED_STYLE = "-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15;";
    private static final String LIGHT_DISPLAY_STYLE = "-fx-background-color: #ffffff; -fx-text-fill: #333333; -fx-border-color: #dddddd; -fx-border-width: 1; -fx-border-radius: 8;";
    private static final String LIGHT_HISTORY_STYLE = "-fx-text-fill: #888888; -fx-font-style: italic;";
    
    // Constants for UI design - Dark Mode
    private static final String DARK_BG = "linear-gradient(to bottom right, #1e1e1e, #2d2d2d)";
    private static final String DARK_NUMBER_STYLE = "-fx-background-color: #3a3a3a; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String DARK_OPERATION_STYLE = "-fx-background-color: #ff9500; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String DARK_SCIENTIFIC_STYLE = "-fx-background-color: #2c2c2c; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String DARK_MEMORY_STYLE = "-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;";
    private static final String DARK_TOGGLE_STYLE = "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15;";
    private static final String DARK_TOGGLE_SELECTED_STYLE = "-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 15;";
    private static final String DARK_DISPLAY_STYLE = "-fx-background-color: #2a2a2a; -fx-text-fill: #ffffff; -fx-border-color: #444444; -fx-border-width: 1; -fx-border-radius: 8;";
    private static final String DARK_HISTORY_STYLE = "-fx-text-fill: #aaaaaa; -fx-font-style: italic;";
    
    // Button sizes
    private static final double BUTTON_SIZE = 65;
    private static final double BUTTON_SPACING = 8;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Modern Calculator");

        // Create display with improved styling
        display = new TextField();
        display.setEditable(false);
        display.setPrefHeight(70);
        display.setFont(Font.font("Roboto", FontWeight.BOLD, 24));
        display.setAlignment(Pos.CENTER_RIGHT);
        applyStyle(display, LIGHT_DISPLAY_STYLE, DARK_DISPLAY_STYLE);
        
        // Create expression history label
        expressionHistory = new Label();
        expressionHistory.setFont(Font.font("Roboto", FontWeight.NORMAL, 14));
        expressionHistory.setAlignment(Pos.CENTER_RIGHT);
        expressionHistory.setMaxWidth(Double.MAX_VALUE);
        applyStyle(expressionHistory, LIGHT_HISTORY_STYLE, DARK_HISTORY_STYLE);
        
        // Set initial display width
        display.setPrefWidth(4 * BUTTON_SIZE + 5 * BUTTON_SPACING);

        // Create toggle buttons container
        HBox toggleContainer = new HBox(10);
        toggleContainer.setAlignment(Pos.CENTER);
        
        // Create toggle button for scientific mode
        scientificToggle = new ToggleButton("Scientific");
        applyStyle(scientificToggle, LIGHT_TOGGLE_STYLE, DARK_TOGGLE_STYLE);
        scientificToggle.setPrefHeight(40);
        scientificToggle.setOnAction(e -> {
            isScientificMode = scientificToggle.isSelected();
            updateToggleStyle(scientificToggle, isScientificMode);
            updateLayout();
        });
        
        // Create toggle button for theme
        themeToggle = new ToggleButton("Dark Mode");
        applyStyle(themeToggle, LIGHT_TOGGLE_STYLE, DARK_TOGGLE_STYLE);
        themeToggle.setPrefHeight(40);
        themeToggle.setOnAction(e -> {
            isDarkMode.set(themeToggle.isSelected());
            updateToggleStyle(themeToggle, isDarkMode.get());
            updateTheme();
        });
        
        toggleContainer.getChildren().addAll(scientificToggle, themeToggle);
        
        // Create a VBox for display and history
        VBox displayContainer = new VBox(5);
        displayContainer.getChildren().addAll(expressionHistory, display);
        
        // Create basic button grid
        GridPane basicGrid = createBasicButtonGrid();
        
        // Create memory button grid
        GridPane memoryGrid = createMemoryButtonGrid();
        
        // Create scientific button grid
        scientificGrid = createScientificButtonGrid();
        
        // Create the top panel with display and toggle buttons
        VBox topPanel = new VBox(10);
        topPanel.setPadding(new Insets(15));
        topPanel.getChildren().addAll(displayContainer, toggleContainer);
        
        // Create the button container
        VBox buttonContainer = new VBox(10);
        buttonContainer.setPadding(new Insets(0, 15, 15, 15));
        buttonContainer.getChildren().addAll(memoryGrid, basicGrid);
        
        // Create the main layout
        mainLayout = new BorderPane();
        mainLayout.setTop(topPanel);
        mainLayout.setCenter(buttonContainer);
        applyBackgroundStyle(mainLayout, LIGHT_BG, DARK_BG);
        
        // Add keyboard support
        Scene scene = new Scene(mainLayout);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        
        // Initial scene setup
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(4 * BUTTON_SIZE + 60);
        primaryStage.setMinHeight(6 * BUTTON_SIZE + 180);
        primaryStage.show();
        
        // Set focus to the scene, not the text field
        scene.getRoot().requestFocus();
    }
    
    private void applyStyle(Control control, String lightStyle, String darkStyle) {
        control.styleProperty().bind(
            isDarkMode.asString().map(isDark -> 
                Boolean.parseBoolean(isDark) ? darkStyle : lightStyle
            )
        );
    }
    
    private void applyBackgroundStyle(Region region, String lightStyle, String darkStyle) {
        region.styleProperty().bind(
            isDarkMode.asString().map(isDark -> 
                Boolean.parseBoolean(isDark) ? 
                "-fx-background-color: " + darkStyle + ";" : 
                "-fx-background-color: " + lightStyle + ";"
            )
        );
    }
    
    private void updateTheme() {
        // Theme is updated automatically through binding
        updateToggleStyle(scientificToggle, scientificToggle.isSelected());
    }
    
    private void updateToggleStyle(ToggleButton button, boolean isSelected) {
        if (isSelected) {
            applyStyle(button, LIGHT_TOGGLE_SELECTED_STYLE, DARK_TOGGLE_SELECTED_STYLE);
        } else {
            applyStyle(button, LIGHT_TOGGLE_STYLE, DARK_TOGGLE_STYLE);
        }
    }

    private void updateLayout() {
        VBox buttonContainer = (VBox) mainLayout.getCenter();
        
        // If scientific mode is enabled, add the scientific grid if it's not already there
        if (isScientificMode) {
            if (!buttonContainer.getChildren().contains(scientificGrid)) {
                buttonContainer.getChildren().add(scientificGrid);
                
                // Increase the width of the display when in scientific mode
                display.setPrefWidth((8 * BUTTON_SIZE + 9 * BUTTON_SPACING));
                
                // Update window size for scientific calculator
                primaryStage.setWidth(8 * BUTTON_SIZE + 9 * BUTTON_SPACING + 50);
                primaryStage.setHeight(10 * BUTTON_SIZE + 220);
            }
        } else {
            // If scientific mode is disabled, remove the scientific grid if it's there
            buttonContainer.getChildren().remove(scientificGrid);
            
            // Restore the original width of the display
            display.setPrefWidth(4 * BUTTON_SIZE + 5 * BUTTON_SPACING);
            
            // Restore original window size for basic calculator
            primaryStage.setWidth(4 * BUTTON_SIZE + 5 * BUTTON_SPACING + 50);
            primaryStage.setHeight(8 * BUTTON_SIZE + 200);
        }
        
        // Force layout update
        primaryStage.sizeToScene();
    }

    private GridPane createBasicButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(BUTTON_SPACING);
        grid.setVgap(BUTTON_SPACING);

        String[][] buttonData = {
            {"7", "8", "9", "÷", LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_OPERATION_STYLE, 
                DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_OPERATION_STYLE},
            {"4", "5", "6", "×", LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_OPERATION_STYLE,
                DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_OPERATION_STYLE},
            {"1", "2", "3", "-", LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_OPERATION_STYLE,
                DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_OPERATION_STYLE},
            {"0", ".", "C", "+", LIGHT_NUMBER_STYLE, LIGHT_NUMBER_STYLE, LIGHT_OPERATION_STYLE, LIGHT_OPERATION_STYLE,
                DARK_NUMBER_STYLE, DARK_NUMBER_STYLE, DARK_OPERATION_STYLE, DARK_OPERATION_STYLE},
            {"=", "⌫", "(", ")", LIGHT_OPERATION_STYLE, LIGHT_OPERATION_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE,
                DARK_OPERATION_STYLE, DARK_OPERATION_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE}
        };

        for (int row = 0; row < buttonData.length; row++) {
            for (int col = 0; col < 4; col++) {
                String label = buttonData[row][col];
                String lightStyle = buttonData[row][col + 4];
                String darkStyle = buttonData[row][col + 8];
                
                Button button = createStyledButton(label, lightStyle, darkStyle);
                grid.add(button, col, row);
                
                // Make equal button larger in future iterations if needed
                if (label.equals("=")) {
                    // We could span multiple columns here if desired
                }
            }
        }

        return grid;
    }
    
    private GridPane createMemoryButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(BUTTON_SPACING);
        grid.setVgap(BUTTON_SPACING);
        
        // Memory buttons with their styles
        String[][] buttonData = {
            {"MC", "MR", "M+", "M-", 
             LIGHT_MEMORY_STYLE, LIGHT_MEMORY_STYLE, LIGHT_MEMORY_STYLE, LIGHT_MEMORY_STYLE,
             DARK_MEMORY_STYLE, DARK_MEMORY_STYLE, DARK_MEMORY_STYLE, DARK_MEMORY_STYLE}
        };
        
        for (int row = 0; row < buttonData.length; row++) {
            for (int col = 0; col < 4; col++) {
                String label = buttonData[row][col];
                String lightStyle = buttonData[row][col + 4];
                String darkStyle = buttonData[row][col + 8];
                
                Button button = createStyledButton(label, lightStyle, darkStyle);
                grid.add(button, col, row);
            }
        }
        
        return grid;
    }

    private GridPane createScientificButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(BUTTON_SPACING);
        grid.setVgap(BUTTON_SPACING);

        // Scientific buttons with their styles - including our new functions (asin, acos, atan, log)
        String[][] buttonData = {
            {"sin", "cos", "tan", "ln", 
             LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE,
             DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE},
            {"asin", "acos", "atan", "log", 
             LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE,
             DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE},
            {"π", "e", "i", "φ", 
             LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE,
             DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE},
            {"x²", "x³", "√x", "∛x", 
             LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE,
             DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE},
            {"xʸ", "1/x", "%", "!",
             LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE, LIGHT_SCIENTIFIC_STYLE,
             DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE, DARK_SCIENTIFIC_STYLE}
        };

        for (int row = 0; row < buttonData.length; row++) {
            for (int col = 0; col < 4; col++) {
                String label = buttonData[row][col];
                String lightStyle = buttonData[row][col + 4];
                String darkStyle = buttonData[row][col + 8];
                
                Button button = createStyledButton(label, lightStyle, darkStyle);
                grid.add(button, col, row);
            }
        }

        return grid;
    }

    private Button createStyledButton(String label, String lightStyle, String darkStyle) {
        Button button = new Button(label);
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        applyStyle(button, lightStyle, darkStyle);
        
        // Add drop shadow effect for 3D look
        DropShadow shadow = new DropShadow();
        shadow.setRadius(4.0);
        shadow.setOffsetX(1.0);
        shadow.setOffsetY(1.0);
        shadow.setColor(Color.color(0.4, 0.4, 0.4, 0.3));
        button.setEffect(shadow);
        
        // Add click animation
        button.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });
        
        button.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        button.setOnAction(e -> handleButtonClick(label));
        return button;
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "C":
                clearDisplay();
                break;
            case "=":
                evaluateExpression();
                break;
            case ".":
                addDecimalPoint();
                break;
            case "⌫": // Backspace
                handleDelete();
                break;
            case "÷":
                appendOperator("/");
                break;
            case "×":
                appendOperator("*");
                break;
            case "+":
            case "-":
                appendOperator(label);
                break;
            case "(":
            case ")":
                currentInput.append(label);
                updateDisplay();
                break;
            // Memory operations
            case "MC":
                memoryValue = 0.0;
                hasMemory = false;
                updateDisplay();
                break;
            case "MR":
                if (hasMemory) {
                    currentInput.append(String.valueOf(memoryValue));
                    updateDisplay();
                }
                break;
            case "M+":
                if (currentInput.length() > 0) {
                    try {
                        memoryValue += Double.parseDouble(evaluateCurrentInput());
                        hasMemory = true;
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                }
                break;
            case "M-":
                if (currentInput.length() > 0) {
                    try {
                        memoryValue -= Double.parseDouble(evaluateCurrentInput());
                        hasMemory = true;
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                }
                break;
            // Scientific operations - including our new functions
            case "sin":
            case "cos":
            case "tan":
            case "asin":
            case "acos":
            case "atan":
            case "log":
            case "ln":
                applyFunction(label);
                break;
            case "π":
                currentInput.append("PI");
                updateDisplay();
                break;
            case "e":
                currentInput.append("E");
                updateDisplay();
                break;
            case "i":
                currentInput.append("i");
                updateDisplay();
                break;
            case "φ":
                currentInput.append("PHI");
                updateDisplay();
                break;
            case "x²":
                applyPower(2);
                break;
            case "x³":
                applyPower(3);
                break;
            case "√x":
                applyFunction("sqrt");
                break;
            case "∛x":
                if (currentInput.length() > 0) {
                    String expr = currentInput.toString();
                    expr = "("+expr+")^(1.0/3)";
                    currentInput.setLength(0);
                    currentInput.append(expr);
                    updateDisplay();
                }
                break;
            case "xʸ":
                if (currentInput.length() > 0) {
                    currentInput.append("^");
                    updateDisplay();
                }
                break;
            case "1/x":
                if (currentInput.length() > 0) {
                    String expr = currentInput.toString();
                    currentInput.setLength(0);
                    currentInput.append("(1.0/").append(expr).append(")");
                    updateDisplay();
                }
                break;
            case "%":
                appendOperator("%");
                break;
            case "!":
                if (currentInput.length() > 0) {
                    currentInput.append("!");
                    updateDisplay();
                }
                break;
            default:
                // Handle numbers and other input
                currentInput.append(label);
                updateDisplay();
        }
    }
    
    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        
        // Prevent the TextField from getting focus when typing
        if (event.getTarget() instanceof TextField) {
            event.consume();
        }
        
        if (code.isDigitKey()) {
            handleButtonClick(code.getName());
            event.consume();
        } else if (code == KeyCode.PERIOD || code == KeyCode.DECIMAL) {
            handleButtonClick(".");
            event.consume();
        } else if (code == KeyCode.EQUALS || code == KeyCode.ENTER) {
            handleButtonClick("=");
            event.consume();
        } else if (code == KeyCode.PLUS || code == KeyCode.ADD) {
            handleButtonClick("+");
            event.consume();
        } else if (code == KeyCode.MINUS || code == KeyCode.SUBTRACT) {
            handleButtonClick("-");
            event.consume();
        } else if (code == KeyCode.ASTERISK || code == KeyCode.MULTIPLY) {
            handleButtonClick("×");
            event.consume();
        } else if (code == KeyCode.SLASH || code == KeyCode.DIVIDE) {
            handleButtonClick("÷");
            event.consume();
        } else if (code == KeyCode.BACK_SPACE) {
            handleButtonClick("⌫");
            event.consume();
        } else if (code == KeyCode.ESCAPE) {
            handleButtonClick("C");
            event.consume();
        } else if (code == KeyCode.OPEN_BRACKET || code == KeyCode.LEFT_PARENTHESIS) {
            handleButtonClick("(");
            event.consume();
        } else if (code == KeyCode.CLOSE_BRACKET || code == KeyCode.RIGHT_PARENTHESIS) {
            handleButtonClick(")");
            event.consume();
        } else if (code == KeyCode.UP) {
            navigateHistory(-1);
            event.consume();
        } else if (code == KeyCode.DOWN) {
            navigateHistory(1);
            event.consume();
        }
    }
    
    private void navigateHistory(int direction) {
        if (calculationHistory.isEmpty()) return;
        
        historyIndex += direction;
        
        if (historyIndex < 0) historyIndex = 0;
        if (historyIndex >= calculationHistory.size()) historyIndex = calculationHistory.size() - 1;
        
        currentInput.setLength(0);
        currentInput.append(calculationHistory.get(historyIndex));
        updateDisplay();
    }
    
    private void applyFunction(String function) {
        if (currentInput.length() > 0) {
            // Apply function to the current expression
            String expr = currentInput.toString();
            currentInput.setLength(0);
            currentInput.append(function).append("(").append(expr).append(")");
        } else {
            // If no input, just add the function name with open parenthesis
            currentInput.append(function).append("(");
        }
        updateDisplay();
    }
    
    private void applyPower(int power) {
        if (currentInput.length() > 0) {
            String expr = currentInput.toString();
            currentInput.setLength(0);
            currentInput.append("(").append(expr).append(")^").append(power);
            updateDisplay();
        }
    }
    
    private void appendOperator(String operator) {
        if (currentInput.length() > 0) {
            char lastChar = currentInput.charAt(currentInput.length() - 1);
            
            // Don't append operator if last character is already an operator
            if ("+-*/^%".indexOf(lastChar) >= 0) {
                // Replace the last operator with the new one
                currentInput.deleteCharAt(currentInput.length() - 1);
                currentInput.append(operator);
            } else {
                currentInput.append(operator);
            }
            updateDisplay();
        } else if (operator.equals("-")) {
            // Allow negative numbers
            currentInput.append(operator);
            updateDisplay();
        }
    }

    private void addDecimalPoint() {
        if (currentInput.length() == 0 || isLastCharacterOperator()) {
            currentInput.append("0.");  // Ensure proper decimal handling
        } else if (!getLastNumber().contains(".")) {
            currentInput.append(".");
        }
        updateDisplay();
    }
    
    private String evaluateCurrentInput() {
        try {
            return calculator.eval(StringToExpression.parseStringTExpression(currentInput.toString())).toString();
        } catch (Exception e) {
            return "Error";
        }
    }

    private void evaluateExpression() {
        if (currentInput.length() == 0) return;
        
        try {
            // Save the expression for history
            String expression = currentInput.toString();
            expressionHistory.setText(expression);
            
            // Evaluate the expression
            String result = calculator.eval(StringToExpression.parseStringTExpression(expression)).toString();
            
            // Update display with the result
            display.setText(result);
            
            // Save to history
            calculationHistory.add(expression);
            historyIndex = calculationHistory.size();
            
            // Reset the current input to the result for chaining calculations
            currentInput.setLength(0);
            currentInput.append(result);
        } catch (Exception e) {
            display.setText("Error: " + e.getMessage());
            currentInput.setLength(0);
        }
    }
    
    private void clearDisplay() {
        currentInput.setLength(0);
        display.setText("");
        expressionHistory.setText("");
    }

    private void handleDelete() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            updateDisplay();
        }
    }
    
    private void updateDisplay() {
        display.setText(currentInput.toString());
        
        // Check for unbalanced parentheses and highlight if needed
        int openCount = 0;
        int closeCount = 0;
        for (int i = 0; i < currentInput.length(); i++) {
            if (currentInput.charAt(i) == '(') openCount++;
            if (currentInput.charAt(i) == ')') closeCount++;
        }
        
        if (openCount != closeCount) {
            display.setStyle(display.getStyle() + "-fx-border-color: #ff6b6b;");
        } else {
            applyStyle(display, LIGHT_DISPLAY_STYLE, DARK_DISPLAY_STYLE);
        }
    }

    private boolean isLastCharacterOperator() {
        if (currentInput.length() == 0) return true;
        char lastChar = currentInput.charAt(currentInput.length() - 1);
        return "+-*/^%".indexOf(lastChar) >= 0;
    }

    private String getLastNumber() {
        String text = currentInput.toString();
        int lastOperatorIndex = -1;
        
        for (int i = text.length() - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if ("+-*/^%()".indexOf(c) >= 0) {
                lastOperatorIndex = i;
                break;
            }
        }
        
        return lastOperatorIndex < 0 ? text : text.substring(lastOperatorIndex + 1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}