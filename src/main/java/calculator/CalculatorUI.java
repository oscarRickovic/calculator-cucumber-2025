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

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("JavaFX Calculator");

        display = new TextField();
        display.setEditable(false);
        display.setPrefHeight(50);
        display.setAlignment(Pos.CENTER_RIGHT);

        // Create toggle button for scientific mode
        scientificToggle = new ToggleButton("Scientific Mode");
        scientificToggle.setOnAction(e -> {
            isScientificMode = scientificToggle.isSelected();
            updateLayout();
        });

        // Create basic button grid
        GridPane basicGrid = createBasicButtonGrid();
        
        // Create scientific button grid
        scientificGrid = createScientificButtonGrid();
        
        // Create control panel with toggle button
        VBox controlPanel = new VBox(10);
        controlPanel.getChildren().add(scientificToggle);
        
        // Create the basic calculator box
        VBox basicCalculatorBox = new VBox(10);
        basicCalculatorBox.setAlignment(Pos.CENTER);
        basicCalculatorBox.setPadding(new Insets(10));
        basicCalculatorBox.getChildren().addAll(display, controlPanel, basicGrid);
        
        // Create the main layout as HBox to place scientific grid on the right
        mainLayout = new HBox(10);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().add(basicCalculatorBox);
        
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
                primaryStage.setWidth(550); // Wider window for scientific calculator
            }
        } else {
            // If scientific mode is disabled, remove the scientific grid if it's there
            mainLayout.getChildren().remove(scientificGrid);
            primaryStage.setWidth(300); // Original width for basic calculator
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

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "C", "+",
                "="
        };

        int row = 0, col = 0;
        for (String label : buttons) {
            Button button = createButton(label);
            grid.add(button, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        // Backspace button
        Button deleteButton = createButton("DEL");
        grid.add(deleteButton, 3, row);

        return grid;
    }

    private GridPane createScientificButtonGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Scientific buttons - first row
        String[] firstRow = {"sin", "cos", "tan", "π"};
        for (int i = 0; i < firstRow.length; i++) {
            Button button = createButton(firstRow[i]);
            grid.add(button, i, 0);
        }

        // Scientific buttons - second row
        String[] secondRow = {"asin", "acos", "atan", "e"};
        for (int i = 0; i < secondRow.length; i++) {
            Button button = createButton(secondRow[i]);
            grid.add(button, i, 1);
        }

        // Scientific buttons - third row
        String[] thirdRow = {"log", "ln", "x²", "x³"};
        for (int i = 0; i < thirdRow.length; i++) {
            Button button = createButton(thirdRow[i]);
            grid.add(button, i, 2);
        }

        // Scientific buttons - fourth row
        String[] fourthRow = {"√x", "∛x", "xʸ", "1/x"};
        for (int i = 0; i < fourthRow.length; i++) {
            Button button = createButton(fourthRow[i]);
            grid.add(button, i, 3);
        }

        // Scientific buttons - fifth row
        String[] fifthRow = {"(", ")", "!", "%"};
        for (int i = 0; i < fifthRow.length; i++) {
            Button button = createButton(fifthRow[i]);
            grid.add(button, i, 4);
        }

        // Memory buttons row
        HBox memoryBox = new HBox(5);
        String[] memoryButtons = {"MC", "MR", "M+", "M-"};
        for (String label : memoryButtons) {
            Button button = createButton(label);
            memoryBox.getChildren().add(button);
        }
        grid.add(memoryBox, 0, 5, 4, 1);

        // Angle unit toggle button
        Button angleButton = createButton("DEG");
        grid.add(angleButton, 0, 6);

        return grid;
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setPrefSize(50, 50);
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
                    // Le calcul se fera lors de l'appui sur "="
                }
                break;
            case "x³":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText(currentValue + "³");
                    // Le calcul se fera lors de l'appui sur "="
                }
                break;
            case "√x":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText("√" + currentValue);
                    // Le calcul se fera lors de l'appui sur "="
                }
                break;
            case "∛x":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText("∛" + currentValue);
                    // Le calcul se fera lors de l'appui sur "="
                }
                break;
            case "1/x":
                if (currentInput.length() > 0) {
                    String currentValue = currentInput.toString();
                    display.setText("1/" + currentValue);
                    // Le calcul se fera lors de l'appui sur "="
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
                    // Le calcul se fera lors de l'appui sur "="
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
                    
                    // Très important: on vide la saisie courante pour que 
                    // l'utilisateur puisse saisir l'exposant
                    currentInput.setLength(0);
                    
                    // On stocke la base et l'opération pour reconstruire l'expression complète
                    currentInput.append(currentValue).append("^");
                }
                break;
            case "MC":  // Memory Clear
            case "MR":  // Memory Recall
            case "M+":  // Memory Add
            case "M-":  // Memory Subtract
            case "DEG": // Degree mode toggle
                // Afficher que ces fonctions sont en cours d'implémentation
                display.setText(label + " (WIP)");
                break;
            default:
                // Pour les autres boutons scientifiques qui ne sont pas encore gérés
                if (isScientificButton(label)) {
                    if (currentInput.length() > 0) {
                        String currentValue = currentInput.toString();
                        display.setText(label + "(" + currentValue + ")");
                    } else {
                        display.setText(label);
                    }
                } else {
                    // Comportement normal pour les boutons de la calculatrice de base
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
            // Note: Cette méthode devrait être étendue pour gérer les notations scientifiques
            // comme "9²", "√9", etc. Pour l'instant, elle utilise seulement le parseur existant
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